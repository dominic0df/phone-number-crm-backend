package dhbw.cloudia.phonenumber.control.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Tuple<T, U> {
    private T fistValue;
    private U secondValue;
}
