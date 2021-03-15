import com.sl.common.vo.SysUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

    public static void main(String[] args){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("lh1029"));
    }
}
