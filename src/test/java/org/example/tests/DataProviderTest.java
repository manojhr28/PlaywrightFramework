package org.example.tests;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.objectweb.asm.TypeReference;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DataProviderTest {

    //Normal Data Provider
    @DataProvider(name="SampleData")
    public Object[][] getData(){
        return new Object[][] {{"username1","password1"},{"username2","password2"},{"username3","password3"}};
    }

    @Test(dataProvider = "SampleData")
    public void runTest(String username,String password){
        System.out.println(username);
        System.out.println(password);
    }

    //Using HashMap in Data Provider
    @DataProvider(name="hashMapData")
    public Object[][] hashMapData(){
        HashMap<String,String> user1=new HashMap<>();
        user1.put("username","username1");
        user1.put("password","password1");

        HashMap<String,String> user2=new HashMap<>();
        user2.put("username","username2");
        user2.put("password","password2");

        return new Object[][] {{user1},{user2}};
    }

    @Test(dataProvider = "hashMapData")
    public void runTest2(HashMap<String,String> data){
        System.out.println(data.get("username"));
        System.out.println(data.get("password"));
    }

    @DataProvider(name = "jsonData")
    public static Object[][] getJsonData() throws IOException {

        String jsonContent=new String(Files.readAllBytes(Paths.get(System.getProperty("user.dir")+"/src/test/resources/testDataFile.json")));

        Type type=new TypeToken<List<HashMap<String,String>>>(){}.getType();
        List<HashMap<String,String>> list=new Gson().fromJson(jsonContent,type);

        Object[][] data = new Object[list.size()][1];

        for (int i = 0; i < list.size(); i++) {
            data[i][0] = list.get(i);
        }
        return data;
    }

    @Test(dataProvider = "jsonData")
    public void runTest3(HashMap<String,String> data){
        System.out.println(data.get("username"));
        System.out.println(data.get("password"));
        System.out.println(data.get("age"));
    }


}
