package fr.xebia.extras.it.mappers;

import fr.xebia.extras.beans.CityIn;
import fr.xebia.extras.beans.CityOut;
import fr.xebia.extras.mapper.Factory;

/**
 *
 */
public class BadCustomMapper {


    CityOut notPublicNotStaticMethod(CityIn in, Factory factory){
        return null;
    }

    public static void voidMethod(CityIn in, Factory factory){
        return ;
    }

    public static CityOut noParameterMethod(){
        return null;
    }

    public static CityOut oneParameterMethod( CityIn in){
        return null;
    }

    public static CityOut threeParameterMethod( CityIn in, CityIn in2, CityIn in3){
        return null;
    }

    public static CityOut withoutFactoryMethod( CityIn in, CityIn in2){
        return null;
    }


}
