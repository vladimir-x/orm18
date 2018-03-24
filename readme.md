

 ORM18
 
Предназначен для упрощённой реализации выборок/сохранения/изменения/удаления (CRUD) данных при работе с БД.

Поддерживает сущности с наследованием и простые поля в сущностях.

Позволяет строить объектные запросы с объединением сущностей(join), наложением условий (where, order, having , group by , limit,offset).


Спроектирован, как обёртка над сущностями.


HOW TO:


1) проектируем сущность

 @Entity - метка сущности. должна присутствовать у каждой сущности, у которой есть поля для отображения в базу или метаформу.

 @Inheritance(strategy = InheritanceType.SINGLE_TABLE ) стратегия наследования. Поддреживается две с тратегии

      InheritanceType.SINGLE_TABLE - всё в одной таблице (стратегия по умолчанию )

      InheritanceType.JOINED - базовая сущность в отдельнйо таблице

@Table("имя таблицы в БД. Рекомендуется ссылаться на константу в классе")



Описание полей сущности производится через аннотации:

@Id - указание , что это колонка ID 
@Column(name = "id") - имя (поля)колонки в БД


Пример аннотированной сущности

    @Entity
	@Table(name = Driver.TABLE)
	public class Driver{
		
		public static final String TABLE = "driver";
		
		@Id
		@Column(name = "id")
		private Integer id;
		
		@Column(name = "fullname")
		private String fullname;
		
		@Column(name = "age") 
		private Integer age;

		@Column(name = "car_id") 
		private Integer carId;
		
		public Driver() {
			
		}
	}


2) Сохранение/Изменение/Удаление

Сохранение/Изменение

 EntityManager.save(entityObject);


При сохранении определяет операцию Create/Update по наличию объекта с ID в таблице сущности.

Рекусривно сохраняет все сущности , задействованные в наследовании.


Удаление

	EntityManager.delete(entityObject);


3) Загрузка сущностей ( и любых данных)


 Большинство простых загрузчиков реализованы в виде методов обёрток в классе

  EntityManager.loadXXXXX

Методы делятся на два типа:

    Возвращающие (возвращают Сущность как результат)

    Вкладывающие (изменяют заданный объект сущности)


 Примеры составления запросов с выборкой данных различной сложности представлены в ru.dude.orm.sample.Examples на примере сущностей Car и Driver.
 Для составления условий используются статические модели сущностей "Car_.class", генерирующиеся мавеном при компиляции автоматически.


3.1) Загрузка по ID

//возвращающий

Car car = EntityManager.loadByID(Car.class,11);


//вкладываемый

Car car = new Car();
EntityManager.loadByID(Car.class,11,car);


3.1) Загрузка сущности с условием


		From<Car> from = new From<>(Car.class);
        Clause clause = ClauseBuilder.and(
                ClauseBuilder.more(from.get(Car_.id), 0),
                ClauseBuilder.eq(from.get(Car_.id), 11)
        );

        // возвращающий
        List<Car> cars = EntityManager.loadByFromClause(from, clause);
        System.out.println(cars);

        //вкладываемый
        Car setInner = new Car();
        EntityManager.loadOneByFromClause(from, clause, setInner);
        System.out.println(setInner);


3.2) Загрузка с join

// джойн с простым условием по равенству значений полей
		From<Driver> from1 = new From<>(Driver.class);
        Join carJoin1 = from1.join(Car.class, Car_.id, from1.get(Driver_.carId));
        Driver res1 = EntityManager.loadOneByFromClause(from1,
                ClauseBuilder.and(
                        ClauseBuilder.more(from1.get(Driver_.id), 11),
                        ClauseBuilder.more(carJoin1.get(Car_.id), 0)
                )
        );
        System.out.println(res1);


3.3) Сложный запрос с конструктором Builder

		Builder builder = new Builder();

        From<Car> from = new From<>(Car.class);

        SelectorEntity<Car> selector = builder.getSelectBuilder().entity(from);

        builder.select(selector);
        builder.from(from);
        builder.where(ClauseBuilder.more(from.get(Car_.id), 11));
        builder.orderBy(new Order().add(Dir.ASC, from.get(Car_.id)));
        builder.limitOffset(new LimitOffset(1000, 0));

        // возвращающий
        EntityMapper<Car> mapper1 = new EntityMapper(selector);
        builder.mapper(mapper1);
        builder.build();

        List<Car> result1 = mapper1.getResult();
        System.out.println(result1);

        // вкладываемый
        Car res2 = new Car();
        EntityMapper<Car> mapper2 = new EntityMapper(selector);
        mapper2.setResultInstance(res2);
        builder.mapper(mapper2);
        builder.build();

        System.out.println(res2);


3.4) Извлечение данных, без прямого отображения в сущность

		Builder builder = new Builder();

        From<Driver> from = new From<>(Driver.class);

        SelectorSimple selector = builder.getSelectBuilder().Simple(
                from.get(Driver_.id),
                from.get(Driver_.carId),
                from.get(Driver_.fullname)
        );
        TupleMapper mapper = new TupleMapper(selector);

        builder.select(selector);
        builder.from(from);
        builder.limitOffset(new LimitOffset(10));

        builder.mapper(mapper);
        builder.build();

        List<TupleMapper.Tuple> result = mapper.getResult();
        System.out.println(result);

        for (TupleMapper.Tuple tuple : result) {

            //Пример извлечения
            Integer id = tuple.get(from.get(Driver_.id));
            Integer carId = tuple.get(from.get(Driver_.carId));
            String fullName = tuple.get(from.get(Driver_.fullname));

            System.out.println(id + " " + carId + " " + fullName);
        }


3.5) Группировки

		Builder builder = new Builder();

        From<Driver> from = new From<>(Driver.class);

        SelectBuilder sb = builder.getSelectBuilder();
        SelectorFunction<Long> countField = sb.Count();
        SelectorSimple selector = sb.Complex(
                sb.MakeSelectableJF(from.get(Driver_.fullname)),
                countField
        );
        TupleMapper mapper = new TupleMapper(selector);

        builder.select(selector);
        builder.from(from);
       
        builder.groupBy(new GroupBy(from.get(Driver_.fullname)));

        builder.having(ClauseBuilder.more(countField, 0));

        builder.orderBy(new Order().add(Dir.ASC, countField));
        builder.limitOffset(new LimitOffset(10));

        builder.mapper(mapper);
        builder.build();

        List<TupleMapper.Tuple> result = mapper.getResult();
        System.out.println(result);

        for (TupleMapper.Tuple tuple : result) {

            //Пример извлечения
            String fullname = tuple.get(from.get(Driver_.fullname));
            Long count = tuple.get(countField);

            System.out.println(fullname + " : "+ count);
        }

3.5) Загрузка количества записей

		From<Driver> from = new From<>(Driver.class);
        Clause clause = ClauseBuilder.and(
                ClauseBuilder.more(from.get(Driver_.id), 0),
                ClauseBuilder.more(from.get(Driver_.id), 11)
        );

        Long result = EntityManager.countByFromClause(from, clause);
        System.out.println(result);


4) Нативный SQL (не рекомендуется к использованию)


		Builder builder = new Builder();
        ResultSet rs = builder.nativeSql("Select "
                + EntityUtils.getFieldColumn(Car_.id) + ","
                + EntityUtils.getFieldColumn(Car_.brand)
                + " from " + EntityUtils.getTableName(Car.class) + " limit 10");

        System.out.println(rs);
        builder.closeResultSet(rs);



 
