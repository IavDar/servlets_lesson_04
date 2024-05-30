package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.Car;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import repository.CarRepositoryDB;
import repository.CarRepositoryHibernate;
import service.CarService;
import service.CarServiceImpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarServlet extends HttpServlet {

    private CarService service = new CarServiceImpl(new CarRepositoryHibernate());

    // Получение автомобиля или всех автомобилей:
    // GET http://localhost:8080/cars?id=3 - один авто
    // GET http://localhost:8080/cars - все авто

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // req - объект запроса, он содержит всю информацию, которую прислал нам клиент
        // resp - объект ответа, который будет отправлен клиенту после того, как
        //        отработает наш метод. Этот объект мы наполняем информацией, которую
        //        мы и хотим отправить клиенту

        Map<String, String[]> params = req.getParameterMap();
        // "id" : ["3"]

        if (params.isEmpty()) {
            List<Car> carList = service.getAll();
            resp.getWriter().write(carList.toString());

        } else {
            Long id = Long.parseLong(params.get("id")[0]);
            Car car = service.getById(id);
            resp.getWriter().write(car.toString());
        }
    }

    // Сохранение автомобиля в БД:
    // POST http://localhost:8080/cars - в теле будет приходить объект автомобиля

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        Car car = mapper.readValue(req.getReader(), Car.class);
        service.save(car);
        resp.getWriter().write(car.toString());
    }

    // Изменение уже существующего в БД автомобиля:
    // PUT http://localhost:8080/cars - в теле будет приходить объект автомобиля, подлежащий изменению

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> params = req.getParameterMap();

        if (params.isEmpty()) {
            // TODO домашнее задание
        } else {
            Long id = Long.parseLong(params.get("id")[0]);
            BigDecimal newPrice = new BigDecimal(params.get("newPrice")[0]);
            Car car = service.getById(id);

            car.setPrice(newPrice);
            service.update(car);

            resp.getWriter().write(car.toString());
        }
    }

    // Удаление автомобиля из БД
    // DELETE http://localhost:8080/cars?id=3

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO домашнее задание

        Map<String, String[]> params = req.getParameterMap();

        if (params.isEmpty()) {
            // TODO домашнее задание
            resp.getWriter().write("Id is empty");
        } else {
            Long id = Long.parseLong(params.get("id")[0]);

            service.delete(id);

            resp.getWriter().write("DELETED");
        }
    }
}