import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args)  throws ClassNotFoundException {
        JSONParser parser = new JSONParser();
        String name_suffix, first_name, last_name, drugs, email, gender, country, city, street, house_number, p1_f, p1_l, p2_f, p2_l;
        name_suffix = first_name = last_name = drugs = gender = email = country = city = street = house_number = p1_f = p1_l = p2_f = p2_l = "";
        int id = 0;
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;
        try
        {
            connection = DriverManager.getConnection("jdbc:sqlite:mock_data.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            statement.executeUpdate("drop table if exists mock_data");
            statement.executeUpdate("create table mock_data (id int, name_suffix string, first_name string, last_name string, drugs string, email string, gender string, country string, city string, street string, house_number string, p1_f string, p1_l string, p2_f string, p2_l string)");
            try {
                Object obj = parser.parse(new FileReader("MOCK_DATA-5.json"));

                JSONArray jsonObjects =  (JSONArray) obj;

                System.out.println();
                System.out.println();
                for (Object o : jsonObjects) {
                    JSONObject jsonObject = (JSONObject) o;

                    name_suffix = (String) jsonObject.get("name_suffix");
                    first_name = (String) jsonObject.get("first_name");
                    last_name = (String) jsonObject.get("last_name");

                    JSONArray jsonArrayDrugs = (JSONArray) jsonObject.get("drugs");
                    if(jsonArrayDrugs != null) {
                        drugs = jsonArrayDrugs.toString();
                    }

                    email = (String) jsonObject.get("email");
                    gender = (String) jsonObject.get("gender");

                    JSONObject jsonObjectAddress = (JSONObject) jsonObject.get("address");
                    country = (String) jsonObjectAddress.get("country");
                    city = (String) jsonObjectAddress.get("city");
                    street = (String) jsonObjectAddress.get("street");
                    house_number = (String) jsonObjectAddress.get("house_number");

                    JSONObject jsonObjectParents = (JSONObject) jsonObject.get("parents");
                    JSONObject parent_one = (JSONObject)jsonObjectParents.get("parent_one");
                    JSONObject parent_two = (JSONObject)jsonObjectParents.get("parent_two");
                    if(parent_one != null){
                        p1_f = (String) parent_one.get("first_name");
                        p1_l = (String) parent_one.get("last_name");
                    }
                    if(parent_two != null){
                        p2_f = (String) parent_two.get("first_name");
                        p2_l = (String) parent_two.get("last_name");
                    }

                    String sql = "insert into mock_data VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setInt(1, id);
                    ps.setString(2, name_suffix);
                    ps.setString(3, first_name);
                    ps.setString(4, last_name);
                    ps.setString(5, drugs);
                    ps.setString(6, email);
                    ps.setString(7, gender);
                    ps.setString(8, country);
                    ps.setString(9, city);
                    ps.setString(10, street);
                    ps.setString(11, house_number);
                    ps.setString(12, p1_f);
                    ps.setString(13, p1_l);
                    ps.setString(14, p2_f);
                    ps.setString(15, p2_l);

                    ps.executeUpdate();

                    ++id;
                    name_suffix=first_name=last_name=drugs=gender=email=country=city=street=house_number=p1_f=p1_l=p2_f=p2_l = "";
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println();

            String userChoice = menu();
            switch(userChoice)
            {
                case "A":
                    JSONObject lastNameObject = new JSONObject();
                    System.out.println("lastName: ");
                    String name;
                    Scanner input = new Scanner(System.in);
                    name = input.nextLine();

                    String sql = "SELECT * FROM mock_data WHERE last_name = ?";
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setString(1, name);
                    ResultSet rs= pstmt.executeQuery();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    int columnsNumber = rsmd.getColumnCount();
                    JSONArray ja = new JSONArray();
                    id = 0;
                    while (rs.next()) {
                        for (int i = 2; i <= columnsNumber; i++) {
                            JSONObject newobj = new JSONObject();
                            newobj.put(rsmd.getColumnName(i), rs.getString(i));

                            ja.add(newobj);
                        }
                        System.out.println();
                        JSONArray clone = (JSONArray) ja.clone();
                        lastNameObject.put(id,clone);
                        ja.clear();
                        ++id;
                    }

                    System.out.println(lastNameObject.toJSONString());
                    Files.write(Paths.get("feladat_A.json"), lastNameObject.toJSONString().getBytes());
                    break;
                case "B":
                    rs = statement.executeQuery("select count(*) from mock_data;");
                    System.out.println("Adatok mennyisége: "+rs.getString(1));
                    break;
                case "C":
                    System.out.println("Országok listája: ");
                    rs = statement.executeQuery("select country from mock_data group by country;");
                    rsmd = rs.getMetaData();
                    columnsNumber = rsmd.getColumnCount();
                    while (rs.next()) {
                        System.out.print(rs.getString(1)+"\n");
                    }
                    break;
                case "D":
                    JSONObject parentObject = new JSONObject();
                    Map<String, String> parentMap = new HashMap<>();

                    rs = statement.executeQuery("select * from mock_data where p2_f='' and p2_l='';");
                    rsmd = rs.getMetaData();
                    columnsNumber = rsmd.getColumnCount();
                    ja = new JSONArray();
                    id = 0;
                    while (rs.next()) {
                        for (int i = 2; i <= columnsNumber; i++) {
                            JSONObject newobj = new JSONObject();
                            newobj.put(rsmd.getColumnName(i), rs.getString(i));

                            ja.add(newobj);
                            System.out.print(rs.getString(i)+" ");
                        }
                        System.out.println();
                        JSONArray clone = (JSONArray) ja.clone();
                        parentObject.put(id,clone);
                        ja.clear();
                        ++id;

                    }
                    Files.write(Paths.get("feladat_D.json"), parentObject.toJSONString().getBytes());
                    break;
                case "E":
                    JSONObject genderObject = new JSONObject();
                    Map<String, Integer> genderMap = new HashMap<>();

                    rs = statement.executeQuery("select gender, count(*) from mock_data where gender is not null group by gender;");
                    rsmd = rs.getMetaData();
                    columnsNumber = rsmd.getColumnCount();
                    while (rs.next()) {
                        for (int i = 1; i < columnsNumber; i=i +2) {
                            System.out.print(rs.getString(i)+": ");
                            System.out.println(rs.getString(i+1));
                            genderMap.put(rs.getString(i), rs.getInt(i+1));
                            genderObject.putAll(genderMap);
                        }

                    }

                    Files.write(Paths.get("feladat_E.json"), genderObject.toJSONString().getBytes());
                    break;
                case "F":
                    JSONObject countryObject = new JSONObject();
                    Map<String, String> countryMap = new HashMap<>();
                    rs = statement.executeQuery("select country, group_concat(full_name) from (select country, first_name || ' ' || last_name as full_name from mock_data) group by country;\n");
                    while (rs.next()) {
                        System.out.println(rs.getString(1)+": "+rs.getString(2));
                        countryMap.put(rs.getString(1),rs.getString(2));
                    }
                    countryObject.putAll(countryMap);
                    Files.write(Paths.get("feladat_F.json"), countryObject.toJSONString().getBytes());
                    break;
                case "Q":
                    System.exit(0);
                default:
                    menu();
            }


        }
        catch(SQLException | IOException e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException e)
            {
                System.err.println(e);
            }
        }

    }

    public static String menu() {
        String selection;
        Scanner input = new Scanner(System.in);

        /***************************************************/

        System.out.println("Válasszon az alábbiak közül");
        System.out.println("-------------------------\n");
        System.out.println("A - Szűrés lastname-re (billről)");
        System.out.println("B - Adatok mennyisége");
        System.out.println("C - Országok listája");
        System.out.println("D - Max 1 szülő");
        System.out.println("E - Összes gender és hozzájuk tartozó objektumok számossága");
        System.out.println("F - Egy országhoz tartozó nevek");
        System.out.println("Q - Kilépés");

        selection = input.nextLine();
        return selection.toUpperCase();
    }
}