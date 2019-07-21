/**
 * Created by Pavel on 19/07/2019.
 */
public class Student {

    String name;
    double grade;


    public Student(String name, String grade){

        this.name = name;
        this.grade =  Double.parseDouble(grade);

    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", grade=" + grade +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getName() {
        return name;
    }

    public double getGrade() {
        return grade;
    }

}
