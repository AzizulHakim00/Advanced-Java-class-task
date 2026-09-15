package bd.edu.seu.jerseysee.FootballItems.FootballKits;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Kits {
    private String type; //  player edition  , fan edition or retro kit
    private String name; // jersey
    private String season; // 26-27
    private String club; // barcelona
    private int stock;
    private double price;
    private List<String> size; // s , m , l , xl
    private String bandName ;
    private String catergory; //
    private String country;
    // go on ..
}
