package fr.xebia.extras.it;

import fr.xebia.extras.beans.EnumIn;
import fr.xebia.extras.beans.EnumOut;
import fr.xebia.extras.it.mappers.SimpleMapper;
import fr.xebia.extras.it.utils.Compile;
import fr.xebia.extras.it.utils.IntegrationTestBase;
import fr.xebia.extras.mapper.XMapper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.math.BigInteger;
import java.util.*;

import static org.junit.Assert.*;

/**
 *
 */
@Compile(withClasses = SimpleMapper.class)
public class PrimitivesMapperIT extends IntegrationTestBase {

    public static final String COUCOU = "coucou";

    @Test
    public void mapper_should_convert_string_to_new_string() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        String res = mapper.convert(COUCOU);

        Assert.assertEquals(COUCOU, res);
        Assert.assertFalse(COUCOU == res);
    }

    @Test
    public void mapper_should_convert_null_string_to_null() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        String res = mapper.convert((String)null);

        Assert.assertNull(res);
    }

    @Test
    public void mapper_should_convert_int_to_int() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        int res = mapper.convert(3);

        Assert.assertEquals(3, res);
    }


    @Test
    public void mapper_should_convert_boolean_to_boolean() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        boolean res = mapper.convert(true);

        Assert.assertEquals(true, res);
    }

    @Test
    public void mapper_should_convert_byte_to_byte() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        byte res = mapper.convert(((byte)'c'));

        Assert.assertEquals(((byte)'c'), res);
    }

    @Test
    public void mapper_should_convert_char_to_char() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        char res = mapper.convert(('c'));

        Assert.assertEquals('c', res);
    }

    @Test
    public void mapper_should_convert_char_array_to_new_char_array() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        char[] in = {'c', 'd', 'b'};
        char[] res = mapper.convert(in);

        Assert.assertArrayEquals(in, res);
    }


    @Test
    public void mapper_should_convert_null_char_array_to_null() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        char[] in = null;
        char[] res = mapper.convert(in);

        assertNull(res);
    }

    @Test
    public void mapper_should_convert_boxed_array_to_new_boxed_array() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        Character[] in = {'c', null, 'b'};
        Character[] res = mapper.convert(in);

        org.junit.Assert.assertArrayEquals(in,res);
    }


    @Test
    public void mapper_should_convert_null_boxed_array_to_null() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        Character[] in = null;
        Character[] res = mapper.convert(in);

        assertNull(res);
    }


    @Test
    public void mapper_should_convert_string_array_to_new_string_array() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        String[] in = {"coucou", null, "cocu"};
        String[] res = mapper.convert(in);

        org.junit.Assert.assertArrayEquals(in,res);
    }


    @Test
    public void mapper_should_convert_null_string_array_to_null() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        String[] in = null;
        String[] res = mapper.convert(in);

        assertNull(res);
    }


    @Test
    public void mapper_should_convert_boxed_type_to_boxed_type() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        Integer in = 10;
        Integer res = mapper.convert(in);

        Assert.assertEquals(in, res);
        Assert.assertTrue(in != res);
    }

    @Test
    public void mapper_should_convert_BigInt_to_new_BigInt() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        BigInteger in = new BigInteger("145654326543676");
        BigInteger res = mapper.convertBigInt(in);

        Assert.assertEquals(in, res);
        Assert.assertTrue(in != res);
    }

    @Test
    public void mapper_should_convert_Date_to_new_Date() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        Date in = new Date();
        Date res = mapper.convertDate(in);

        Assert.assertEquals(in, res);
        Assert.assertTrue(in != res);
    }

    @Test
    public void mapper_should_convert_boxed_type_null_to_null() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        Integer in = null;
        Integer res = mapper.convert(in);

        Assert.assertNull(res);
    }

    @Test
    public void mapper_should_convert_List_String_to_list_String() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        List<String> in = new ArrayList<>();
        in.add("coucou");
        in.add(null);
        in.add("coco");
        List<String> res = mapper.convert(in);

        Assert.assertEquals(in, res);
    }


    @Test
    public void mapper_should_convert_HashSet_String_to_HashSet_String() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        HashSet<String> in = new HashSet<>();
        in.add("coucou");
        in.add(null);
        in.add("coco");
        HashSet<String> res = mapper.convert(in);

        Assert.assertEquals(in, res);
    }


    @Test
    public void mapper_should_convert_Set_String_to_HashSet_String() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        HashSet<String> in = new HashSet<>();
        in.add("coucou");
        in.add(null);
        in.add("coco");
        HashSet<String> res = mapper.convertSetToHashSet(in);

        Assert.assertEquals(in, res);
    }

    @Test
    public void mapper_should_convert_TreeSet_String_to_Set_String() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        LinkedHashSet<String> in = new LinkedHashSet<>();
        in.add("coucou");
        in.add(null);
        in.add("coco");
        Set<String> res = mapper.convertLnkedHashSetToSet(in);

        Assert.assertEquals(in, res);
    }

    @Test
    public void mapper_should_convert_HashSet_null_to_null() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        HashSet<String> in = null;
        HashSet<String> res = mapper.convert(in);

        Assert.assertNull(res);
    }

    @Test
    public void mapper_should_convert_map_String_to_map_String() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        Map<String, String> in = new HashMap<>();
        in.put("coucou", "coucou");
        in.put(null, "test");
        in.put("coco", null);
        in.put("ddd", "ddd");
        Map<String, String> res = mapper.convert(in);

        Assert.assertEquals(in, res);
    }

    @Test
    public void mapper_should_convert_map_Date_to_map_Date() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        TreeMap<String, Date> in = new TreeMap<>();
        in.put("coucou", new Date());
        in.put("NoNull", new Date (0));
        in.put("coco", null);
        in.put("ddd", new Date(61223555));
        Map<String, Date> res = mapper.convertStringToDateMap(in);

        Assert.assertEquals(in, res);
    }

    @Test
    public void mapper_should_convert_map_BigInt_to_map_BigInt() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        Map<BigInteger, Date> in = new LinkedHashMap<>();
        in.put(new BigInteger("1234567890987654321"), new Date());
        in.put(new BigInteger("54321234567890"), new Date (0));
        in.put(new BigInteger("98765432345678987654"), null);
        in.put(new BigInteger("767876567898765434890"), new Date(61223555));
        Map<BigInteger, Date> res = mapper.convertBigIntToDateMap(in);

        Assert.assertEquals(in, res);
    }

    @Test
    public void mapper_should_convert_map_null_to_null() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        Map<String, String> in = null;
        Map<String, String> res = mapper.convert(in);

        Assert.assertNull(res);
    }


    @Test
    public void mapper_should_convert_enum_to_enum() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        EnumIn in = EnumIn.VAL_2;
        EnumOut res = mapper.convert(in);

        Assert.assertEquals(in.name(), res.name());
    }

    @Test
    public void mapper_should_convert_enum_null_to_null() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        EnumIn in = null;
        EnumOut res = mapper.convert(in);

        Assert.assertNull(res);
    }


    @Test
    public void mapper_should_convert_twodim_array_to_multidim_array() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        String[][] in = new String[][]{{"coucou", null, "coucou"}, {"coucou", null, "coucou"}};
        String[][] res = mapper.convert(in);

        Assert.assertArrayEquals(in, res);
    }

    @Test
    public void mapper_should_convert_twodim_array_null_to_null() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        String[][] in = null;
        String[][] res = mapper.convert(in);

        Assert.assertNull(res);
    }

    @Test
    @Ignore
    public void mapper_should_convert_three_dim_array_to_three_dim_array() throws Exception{

        SimpleMapper mapper = XMapper.getMapper(SimpleMapper.class);

        String[][][] in = new String[][][]{{{"coucou", null, "coucou"}, {"tutu", "bad","tata"}}, {{"coucou", null, "coucou"}, {"coucou", null, "coucou"}}, {{"BUbu", null, "BUbu"}, {"POUPOU", null, "POUPOU"}}};
        String[][][] res = mapper.convert(in);

        Assert.assertArrayEquals(in, res);
    }



}
