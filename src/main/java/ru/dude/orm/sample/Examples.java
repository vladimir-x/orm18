package ru.dude.orm.sample;

import ru.dude.orm.Builder;
import ru.dude.orm.OrmOperationException;
import ru.dude.orm.group.GroupBy;
import ru.dude.orm.selectors.*;
import ru.dude.orm.clauses.Clause;
import ru.dude.orm.clauses.ClauseBuilder;
import ru.dude.orm.join.From;
import ru.dude.orm.join.Join;
import ru.dude.orm.mappers.HashMapMapper;
import ru.dude.orm.mappers.TupleMapper;
import ru.dude.orm.order.Dir;
import ru.dude.orm.order.LimitOffset;
import ru.dude.orm.order.Order;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import ru.dude.orm.EntityManager;
import ru.dude.orm.mappers.EntityMapper;
import ru.dude.orm.mappers.EntityMultiMapper;
import ru.dude.orm.model.util.EntityUtils;
import ru.dude.orm.sample.entity.BodyType;
import ru.dude.orm.sample.entity.Car;
import ru.dude.orm.sample.entity.Car_;
import ru.dude.orm.sample.entity.Driver;
import ru.dude.orm.sample.entity.Driver_;
       
/**
 * Примеры работы с ОРМ
 *
 * @author dude.
 */
public class Examples {

    /**
     * Работа с нативным SQL
     */
    public static void testNativeSQL() {
        try {
            Builder builder = new Builder();
            ResultSet rs = builder.nativeSql("Select "
                    + EntityUtils.getFieldColumn(Car_.id) + ","
                    + EntityUtils.getFieldColumn(Car_.brand)
                    + " from " + EntityUtils.getTableName(Car.class) + " limit 10");

            System.out.println(rs);
            builder.closeResultSet(rs);
        } catch (OrmOperationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сохранение/изменение
     */
    public static void testSave() {
        try {
            new Examples().testCreate();
            new Examples().testUpdate();
        } catch (OrmOperationException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Новая запись
     */
    public void testCreate() throws OrmOperationException {

        for (int i=0;i<10;++i){
        
            Car car = new Car();
            car.fillNew(Car.class.getSimpleName());
            
            car.setBrand("Volvo");
            car.setBody(BodyType.HATCHBACK);
            car.setName("XLX_260");
            car.setColor("black");
                    
            EntityManager.save(car);
            
            
            Driver driver = new Driver();
            driver.fillNew(Driver.class.getSimpleName());
            driver.setAge(27+i);
            driver.setFullname("Petrov-Vodkin");
            driver.setCarId(car.getId());
            
            EntityManager.save(driver);
        }

    }

    /**
     * Обновление записи
     */
    public void testUpdate() throws OrmOperationException {
        From<Driver> from = new From<>(Driver.class);
        List<Driver> drivers = EntityManager.loadByFromClause(from, null);
        for (Driver driver : drivers){
            
            driver.fillEdit();
            driver.setFullname(makeRand(testFullnames));
            EntityManager.save(driver);
            
            Car car = EntityManager.loadByID(Car.class,driver.getCarId());
            
            car.fillEdit();
            car.setColor(makeRand(testColours));
            car.setName(makeRand(testModels));
            
            EntityManager.save(car);
        }
    }

    /**
     * Удаление записи
     */
    public void testDelete() throws OrmOperationException {
        
        Driver driver = EntityManager.loadByID(Driver.class, 1);
        EntityManager.delete(driver);
        
    }

    //******************
    /**
     * Загрузка сущностей из БД
     */
    public static void testLoaders() {
        try {
            new Examples().testById();
            new Examples().testClause();
            new Examples().testBuilder();
            new Examples().testJoin();
            new Examples().testSelectorsEntityJoin();
            new Examples().testSelectorsEntityMapperJoin();
            new Examples().testSelectorsMultiEntity();
            new Examples().testSelectorsHashMap();
            new Examples().testSelectorsTuple();
            new Examples().testCount();
            new Examples().testSelectorsGroupBy();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * по ID, возвращающий результат
     */
    public void testById() throws OrmOperationException {

        Car car = EntityManager.loadByID(Car.class, 1);
        System.out.println(car);
    }

    /**
     * Простой запрос с условиями
     */
    public void testClause() throws OrmOperationException {

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
    }

    /**
     * Запрос с условием, сортировкой, лимитом и смещением
     */
    public void testBuilder() throws OrmOperationException {

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
    }

    /**
     * Запрос с джойном, возвращает сущьность для таблицы From
     */
    public void testJoin() throws OrmOperationException {

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

        //джойн со сложным условием
        From<Driver> from2 = new From<>(Driver.class);
        Join carJoin2 = from2.join(Car.class);
        carJoin2.setOn(ClauseBuilder.and(
                ClauseBuilder.eq(from2.get(Driver_.carId), carJoin1.get(Car_.id)),
                ClauseBuilder.more(carJoin2.get(Car_.id), 0)
        ));
        Driver res2 = EntityManager.loadOneByFromClause(from2, ClauseBuilder.more(from2.get(Driver_.id), 11));
        System.out.println(res2);
    }

    /**
     * Запрос с джойном, Выборка приджойненоый сущности
     *
     */
    public void testSelectorsEntityJoin() throws OrmOperationException {

        Builder builder = new Builder();

        From<Driver> from = new From<>(Driver.class);
        Join<Car> carJoin = from.join(Car.class, Car_.id, from.get(Driver_.carId));

        builder.from(from);
        builder.where(ClauseBuilder.more(from.get(Driver_.id), 11));

        List<Car> result = EntityManager.loadByBuilder(builder, carJoin);
        System.out.println(result);

    }

    /**
     * Запрос с джойном, Выборка приджойненоый сущности , через маппер и
     * селектор
     *
     */
    public void testSelectorsEntityMapperJoin() throws OrmOperationException {

        Builder builder = new Builder();

        From<Driver> from = new From<>(Driver.class);
        Join<Car> carJoin = from.join(Car.class, Car_.id, from.get(Driver_.carId));

        SelectorEntity<Car> carSelector = builder.getSelectBuilder().entity(carJoin);
        EntityMapper<Car> mapper = new EntityMapper(carSelector);

        builder.select(carSelector);
        builder.from(from);
        builder.where(ClauseBuilder.more(from.get(Driver_.id), 11));

        builder.mapper(mapper);
        builder.build();

        List<Car> result = mapper.getResult();
        System.out.println(result);

    }

    /**
     * Запрос с джойном, выборка нескольких сущностей одновременно
     */
    public void testSelectorsMultiEntity() throws OrmOperationException {

        //выборка нескольких сущностей
        Builder builder = new Builder();

        From<Driver> from = new From<>(Driver.class);
        Join<Car> carJoin = from.join(Car.class, Car_.id, from.get(Driver_.carId));

        SelectorEntity<Driver> driverSelector = builder.getSelectBuilder().entity(from);
        SelectorEntity<Car> carSelector = builder.getSelectBuilder().entity(carJoin);
        SelectorMultiEntity multiEntitySelector = builder.getSelectBuilder().MultiEntity(driverSelector, carSelector);

        EntityMapper<Driver> driverMapper = new EntityMapper<>(driverSelector);
        EntityMapper<Car> carMapper = new EntityMapper<>(carSelector);
        EntityMultiMapper mapper = new EntityMultiMapper(driverMapper, carMapper);

        builder.select(multiEntitySelector);
        builder.from(from);
        builder.limitOffset(new LimitOffset(10));

        builder.mapper(mapper);
        builder.build();

        Object[] result = mapper.getResult();
        System.out.println(result);

        List<Driver> drivers = driverMapper.getResult();
        List<Car> cars = carMapper.getResult();
        System.out.println(drivers);
        System.out.println(cars);
    }

    /**
     * Выборка в хэшмэп
     */
    public void testSelectorsHashMap() throws OrmOperationException {
        Builder builder = new Builder();

        From<Driver> from = new From<>(Driver.class);

        SelectorSimple selector = builder.getSelectBuilder().Simple(
                from.get(Driver_.id),
                from.get(Driver_.carId),
                from.get(Driver_.fullname)
        );
        HashMapMapper mapper = new HashMapMapper(selector);

        builder.select(selector);
        builder.from(from);
        builder.limitOffset(new LimitOffset(10));

        builder.mapper(mapper);
        builder.build();

        List<Map<String, Object>> result = mapper.getResult();
        System.out.println(result);
    }

    /**
     * Выборка в tuple (кортэж)
     */
    public void testSelectorsTuple() throws OrmOperationException {
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
    }

    /**
     * Тест количества строк
     *
     * @throws OrmOperationException
     */
    public void testCount() throws OrmOperationException {
        From<Driver> from = new From<>(Driver.class);
        Clause clause = ClauseBuilder.and(
                ClauseBuilder.more(from.get(Driver_.id), 0),
                ClauseBuilder.more(from.get(Driver_.id), 11)
        );

        Long result = EntityManager.countByFromClause(from, clause);
        System.out.println(result);
    }

    /**
     * Тест с группировкой
     *
     * @throws OrmOperationException
     */
    public void testSelectorsGroupBy() throws Exception {

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
    }

    
    private static String makeRand(String[] values){
        return values[rand.nextInt(values.length)];
    }
    
    private static final Random rand = new Random(new Date().getTime());
    private static final String [] testColours = new String[]{"black","white","blue","green"};
    private static final String [] testFullnames = new String[]{"Ivanov V.V.","Petrov A.A.","Sidorov S.S.","Tramp D.D."};
    private static final String [] testModels = new String[]{"CX-50","BM-810","CX-90","LXLXDDDDD"};
}
