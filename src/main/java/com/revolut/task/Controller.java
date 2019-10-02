package com.revolut.task;

import com.google.gson.Gson;
import com.revolut.task.dto.MoneyTransferDTO;
import com.revolut.task.service.TransferService;

import static spark.Spark.*;

public class Controller {

    public static void main(String[] args) {
        Gson gson = new Gson();

        final TransferService transferService;

        port(8080);

        get("/hello", (req, res)->"Hello, world");

        post("/transfer", (request, response) -> {
            MoneyTransferDTO moneyTransferDTO = gson.fromJson(request.body(), MoneyTransferDTO.class);
            transferService.transferMoney(moneyTransferDTO);

            return "The amount transfered succesfuly";
        });

        post("/users", (request, response) -> {
            response.type("application/json");
            User user = new Gson().fromJson(request.body(), User.class);
            userService.addUser(user);

            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS));
        });

        post("/courses", "application/json", (req, res) -> {
            Course course = gson.fromJson(req.body(), Course.class);
            courseDao.add(course);
            res.status(201);
            return course;
        }, gson::toJson);
    }
}
