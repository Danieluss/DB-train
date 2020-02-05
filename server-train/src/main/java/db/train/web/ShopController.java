package db.train.web;

import db.train.persistence.model.Connection;
import db.train.persistence.model.Station;
import db.train.persistence.model.join.StationsConnections;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RequestMapping("/api/shop/")
@RestController
public class ShopController {

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    private static class Money {
        private Double amount;
        private String currency;
    }

    @RequestMapping(value = "/path", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Money order(Connection connection, Station stationFrom, Station stationTo) {

        return new Money();
    }

    @RequestMapping(value = "/commutation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Money order() {

        return new Money();
    }

}
