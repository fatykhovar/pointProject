public class Program {
    public static void main(String[] args) {

    }
//    public double determinant(double [] A){
//        if (A.length != 9)
//            return 0;
//        else{
//            return 1;
//        }
//    }
}



class Point{
    int dim;
    double [] x;

    Point(int dim){
        this.dim = dim;
        this.x = new double[dim];
        for (int i=0;i<dim;i++)
            this.x[i] = 0;
    }

    Point(int dim, double [] x){
        if (x.length != dim){
            throw new IllegalArgumentException("Неверная размерность координаты.");
        }
        this.dim = dim;
        this.x = x;
    }

    double abs(){
        double sum = 0;
        for (int i = 0; i<dim; i++)
            sum += x[i]*x[i];
        return Math.sqrt(sum);
    }

    public static Point add(Point a, Point b){
        if (a.dim != b.dim)
            throw new IllegalArgumentException("Несовпадение размерности координат точек.");
        double [] new_x = new double[a.dim];
        for (int i=0; i < a.dim;i++)
            new_x[i] = a.x[i] + b.x[i];
        return new Point(a.dim, new_x);
    }

    public Point add(Point b){
        return Point.add(this, b);
    }

    static Point sub(Point a, Point b){
        if (a.dim != b.dim)
            throw new IllegalArgumentException("Несовпадение размерности координат точек.");
        double [] new_x = new double[a.dim];
        for (int i=0; i < a.dim;i++)
            new_x[i] = a.x[i] - b.x[i];
        return new Point(a.dim, new_x);
    }

    Point sub(Point b){
        return Point.sub(this, b);
    }

    static Point mult(Point a, double r){
        double [] new_x = new double[a.dim];
        for (int i=0; i < a.dim;i++)
            new_x[i] = a.x[i] * r;
        return new Point(a.dim, new_x);
    }

    Point mult(double r){
        return Point.mult(this, r);
    }

    static double mult(Point a, Point b) {
        if (a.dim != b.dim)
            throw new IllegalArgumentException("Несовпадение размерности координат точек.");
        double sum = 0;
        for (int i=0; i < a.dim;i++)
            sum += a.x[i] * b.x[i];
        return sum;
    }

    double mult(Point b){
        return mult(this, b);
    }

    static Point symAxis(Point a, int i){
        for (int j=0; j < a.dim;j++)
            if (i != j)
                a.x[j] = -a.x[j];
        return a;
    }

    Point symAxis(int i) {
        return symAxis(this, i);
    }

//    String toString(){
//
//    }
}

class Point2D extends Point{
    Point2D(){
        super(2);
    }

    Point2D(double [] x){
        super(2, x);
    }

    static Point2D rot(Point2D p, double phi){
        double r = p.abs();
        double alpha = Math.acos(p.x[0]/r);
        double [] new_x = {r*Math.cos(alpha+phi), r*Math.sin(alpha+phi)};
        return new Point2D(new_x);
    }

    Point2D rot(double phi){
        return rot(this,phi);
    }
}

class Point3D extends Point{
    Point3D(){
        super(3);
    }

    Point3D(double [] x){
        super(3, x);
    }

    static Point3D cross_prod(Point3D p1, Point3D p2){
        double [] new_x = {p1.x[1]*p2.x[2]-p1.x[2]*p2.x[1],
                p1.x[2]*p2.x[0]-p1.x[0]*p2.x[2], p1.x[0]*p2.x[1]-p1.x[1]*p2.x[0]};
        return new Point3D(new_x);
    }

    Point3D cross_prod(Point3D p){
        return cross_prod(this, p);
    }

    private static double determinant(double [][] a){
        if (a.length != 9)
            return 0;
        else
            return Math.abs(a[0][0]*a[1][1]*a[2][2]+
                    a[0][1]*a[1][2]*a[2][0]+a[0][2]*a[1][0]*a[2][1]-
                    a[0][2]*a[1][1]*a[2][0]-a[0][0]*a[1][2]*a[2][1]-
                    a[0][1]*a[1][0]*a[2][2]);
    }

    static double mix_prod(Point3D p1, Point3D p2, Point3D p3){
        double [][] a = {p1.x, p2.x, p3.x};
        return determinant(a);
    }

    double mix_prod(Point3D p1, Point3D p2){
        return mix_prod(this, p1, p2);
    }
}

interface IShape{
    double square();
    double length();
    boolean cross(IShape i);
}

abstract class OpenFigure implements IShape{
    public abstract double square();
}

class Segment extends OpenFigure{
    Point2D start;
    Point2D finish;

    Segment(Point2D s, Point2D f){
        this.start = s;
        this.finish = f;
    }

    Point2D getStart(){
        return this.start;
    }

    void setStart(Point2D a){
        this.start = a;
    }

    Point2D getFinish(){
        return this.finish;
    }

    void setFinish(Point2D a){
        this.finish = a;
    }

    public double length(){
        return Math.sqrt(Math.pow(start.x[0]-start.x[1], 2) + Math.pow(finish.x[0]-finish.x[1], 2));
    }

    public Segment shift(Point2D a){
        return new Segment((Point2D)Point.add(start, a), (Point2D)Point.add(finish, a));
    }


}
