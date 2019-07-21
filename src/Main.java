import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Main {

    private static String JSON_PATH = "resources/students.json";
    private static String CSV_PATH = "resources/students.csv";

    public static void main(String[] args) throws IOException, ParseException {

        List<Student> studentsCSV;
        List<Student> studentsJSON;


        ///parsing files
        System.out.println("Parsing CSV File");
        studentsCSV = readCSV();

        System.out.println("Parsing JSON File");
        studentsJSON = readJSON();


        //filter and write CSV

        System.out.println("Sorting and writing CSV by Grade");
        sortAndFilterByGrade(studentsCSV);
        writeToCSV(studentsCSV, "byGrade");

        System.out.println("Sorting and writing CSV by Name");
        sortAndFilterByName(studentsCSV);
        writeToCSV(studentsCSV, "byName");


        //Filter and write JSON
        /*Note here = there is no need for creating an ArrayList - it's possible to use existing JSONArray to iterate/sort/ and write back into the file, depening
        on implementation and code requirements. I did it in an array, swap back to HashMap / JSONArray in order to re-use filter and write methods. Could have been done differently. */

        System.out.println("Sort and write JSON by Grade");
        sortAndFilterByGrade(studentsJSON);
        writeToJSON(studentsJSON, "byGrade");

        System.out.println("Sort and write JSON by Name");
        sortAndFilterByName(studentsJSON);
        writeToJSON(studentsJSON, "byName");



    }

    public static void sortAndFilterByGrade(List<Student> studentsCSV) throws IOException {

        // sort by grade - using Comparator class - for descending order swap o's
        Collections.sort(studentsCSV, new Comparator<Student>() {
            public int compare(Student o1, Student o2) {
                return Double.valueOf(o1.grade).compareTo(o2.grade);
            }
        });

        System.out.println("List of students to print (sorted) ");
        for (Student s : studentsCSV) {
            System.out.println(s.toString());
        }

    }

    public static void sortAndFilterByName(List<Student> studentsCSV) throws IOException {

        Collections.sort(studentsCSV, new Comparator<Student>() {
            public int compare(Student o1, Student o2) {
                return String.valueOf(o1.name).compareTo(o2.name);
            }
        });

        System.out.println("List of students to print (sorted by name) ");
        for (Student s : studentsCSV) {
            System.out.println(s.toString());
        }


    }

    public static List<Student> readCSV() throws IOException {

        Path pathToFileCSV = Paths.get(CSV_PATH);

        BufferedReader br = new BufferedReader((new FileReader(pathToFileCSV.toFile())));

        String line = br.readLine();

        List<Student> students = new ArrayList<>();
        int index = 0;
        while (line != null) {
            if (index != 0) {  //to skip first line
                String[] attributes = line.split(",");
                Student student = new Student(attributes[0], attributes[1]);
                students.add(student);
                line = br.readLine();
                index++;
            } else {
                line = br.readLine();
                index++;
            }
        }
        return students;
    }


    public static void writeToCSV(List<Student> myList, String sortType) throws IOException {
        FileWriter writer = new FileWriter("resources/students_sorted_" + sortType + ".csv");

        writer.append("Name");   //making columns names
        writer.append(",");
        writer.append("Grade");
        writer.append("\n");

        for (int x = 0; x < myList.size(); x++) {
            String name = myList.get(x).getName();
            String grade = Double.toString(myList.get(x).getGrade());

            writer.append(name);
            writer.append(",");
            writer.append(grade);
            writer.append("\n");

        }
        writer.flush();
        writer.close();
    }

    public static void writeToJSON(List<Student> myList, String sortType) throws IOException {
        System.out.println("\nPrinting list of JSON and building JSON Array");
        JSONArray jsonArray = new JSONArray();
        for(Student s:myList){
            Map b = new LinkedHashMap();  // creating new LinkedHashMap from student object to prevent wrong JSONObject order
            b.put("name", s.getName());
            b.put("grade", s.getGrade());

            System.out.println(s.toString());
            jsonArray.add(b);
        }
        System.out.println(jsonArray.toString());
        FileWriter fileWriter = new FileWriter("resources/students_sorted_" + sortType + ".json");
        fileWriter.write(jsonArray.toJSONString());
        fileWriter.flush();
        fileWriter.close();

        System.out.println("Done");
    }



    public static List<Student> readJSON() throws IOException, ParseException {

        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = new JSONArray();

        List<Student> studentList = new ArrayList<>();

        try {
            Object object = jsonParser.parse(new FileReader(JSON_PATH));

            jsonArray = (JSONArray) object;

            Iterator<JSONObject> iterator = jsonArray.iterator();
            while(iterator.hasNext()){

                JSONObject jsonObject = iterator.next();
                String name = (String) jsonObject.get("name");
                double gradeD = (Double) jsonObject.get("grade");
                String grade = Double.toString(gradeD);

                Student student = new Student(name, grade);
                studentList.add(student);
            }
            System.out.println("done");

        }
        catch (Exception e){
            e.printStackTrace();
        }

        return studentList;

    }

}
