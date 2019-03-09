import nia.chapter5.ByteBufExample;
import org.junit.Test;

/**
 * ByteBufExampleRunner
 *
 * @author liuchang39
 * @date 2019/3/8
 */
public class ByteBufExampleRunner {


    @Test
    public void runSliceByteBufExample() {
        ByteBufExample.sliceByteBuf();
    }

    @Test
    public void testCopyByteBuf() {
        ByteBufExample.copyByteBuf();
    }
}
