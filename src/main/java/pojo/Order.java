package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class Order {

    private ArrayList<String> ingredients;
}
