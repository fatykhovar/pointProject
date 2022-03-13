public class Program {
    public static void main(String[] args) {
        double[] a = {6, 7};
        Point2D start = new Point2D(a);
        double[] b = {3, 4};
        Point2D finish = new Point2D(b);

        Segment s = new Segment(start,finish);
        System.out.println(s.toString());

        double[] c = {8, 9};
        Point2D start1 = new Point2D(c);
        double[] d = {1, 2};

        Point2D finish2 = new Point2D(d);
        Segment s1 = new Segment(start1,finish2);
        s1.shift(start);
        System.out.println(s1.toString());
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

    public String toString(){
        String x_str = "";
        for(int i=0;i<dim;i++) {
            x_str += x[i] + " ";
        }
        return "dim: " + dim + ", x: " + x_str;
    }
}

class Point2D extends Point{
    Point2D(){
        super(2);
    }

    public Point2D(double [] x){
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

interface IMoveable{
    IMoveable shift(Point2D a);
    IMoveable rot(double phi);
    IMoveable symAxis(int i);
}

interface IShape extends IMoveable{
    double square();
    double length();
    boolean cross(IShape i);
}

interface IPolyPoint extends IShape{
    Point2D getP(int i);
    void setP(Point2D p, int i);
}

abstract class OpenFigure implements IShape{
    public double square(){
        return 0;
    }
}

class Segment extends OpenFigure{
    Point2D start;
    Point2D finish;

    public Segment(Point2D s, Point2D f){
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
        return new Segment((Point2D)start.add(a), (Point2D)finish.add(a));
    }

    public Segment rot(double phi){
        return new Segment(start.rot(phi), finish.rot(phi));
    }

    public Segment symAxis(int i){
        return new Segment((Point2D)start.symAxis(i), (Point2D)finish.symAxis(i));
    }

    public boolean cross(IShape i){
        if (!(i instanceof Segment))
            throw new IllegalArgumentException(i + " is not Segment");
        Point2D p1 = start;
        Point2D p2 = finish;
        Point2D p3 = ((Segment) i).start;
        Point2D p4 = ((Segment) i).finish;
        if (p1.x[0] > p2.x[1])
        {
            Point2D tmp = p1;
            p1 = p2;
            p2 = tmp;
        }
        if (p3.x[0] > p4.x[0])
        {
            Point2D tmp = p3;
            p3 = p4;
            p4 = tmp;
        }
        if (p2.x[0] < p3.x[0])
            return false;
        if((p1.x[0] - p2.x[0] == 0) && (p3.x[0] - p4.x[0] == 0)) {
            if (p1.x == p3.x)
                if (!((Math.max(p1.x[1], p2.x[1]) < Math.min(p3.x[1], p4.x[1])) ||
                        (Math.min(p1.x[1], p2.x[1]) > Math.max(p3.x[1], p4.x[1]))))
                    return true;
            return false;
        }
        if (p1.x[0] - p2.x[0] == 0) {
            //найдём Xa, Ya - точки пересечения двух прямых
            double Xa = p1.x[0];
            double A2 = (p3.x[1] - p4.x[1]) / (p3.x[0] - p4.x[0]);
            double b2 = p3.x[1] - A2 * p3.x[0];
            double Ya = A2 * Xa + b2;
            if (p3.x[0] <= Xa && p4.x[0] >= Xa && Math.min(p1.x[1], p2.x[1]) <= Ya &&
                    Math.max(p1.x[1], p2.x[1]) >= Ya)
                return true;
            return false;
        }
        if (p3.x[0] - p4.x[0] == 0) {
            //найдём Xa, Ya - точки пересечения двух прямых
            double Xa = p3.x[0];
            double A1 = (p1.x[1] - p2.x[1]) / (p1.x[0] - p2.x[0]);
            double b1 = p1.x[1] - A1 * p1.x[0];
            double Ya = A1 * Xa + b1;

            if (p1.x[0] <= Xa && p2.x[0] >= Xa && Math.min(p3.x[1], p4.x[1]) <= Ya &&
                    Math.max(p3.x[1], p4.x[1]) >= Ya)
                return true;
            return false;
        }
        //оба отрезка невертикальные
        double A1 = (p1.x[1] - p2.x[1]) / (p1.x[0] - p2.x[0]);
        double A2 = (p3.x[1] - p4.x[1]) / (p3.x[0] - p4.x[0]);
        double b1 = p1.x[1] - A1 * p1.x[0];
        double b2 = p3.x[1] - A2 * p3.x[0];
        if (A1 == A2) {
            return false; //отрезки параллельны
        }
        //Xa - абсцисса точки пересечения двух прямых
        double Xa = (b2 - b1) / (A1 - A2);
        if ((Xa < Math.max(p1.x[0], p3.x[0])) || (Xa > Math.min(p2.x[0], p4.x[0])))
            return false; //точка Xa находится вне пересечения проекций отрезков на ось X
        else
            return true;
    }

    public String toString(){
        return "start: " + start.toString() + ", finish: " + finish.toString() + '\n';
    }
}

class Polyline  extends OpenFigure implements IPolyPoint{
    int n;
    Point2D[] p;

    Polyline(Point2D[] p){
         this.p = p;
    }

    int getN(){
        return n;
    }

    Point2D[] getP(){
        return p;
    }

    public Point2D getP(int i){
        return p[i];
    }

    void setP(Point2D[] p){
        this.p = p;
    }

    public void setP(Point2D p, int i){
        this.p[i] = p;
    }

    public double length(){
        int l = 0;
        Segment[] polyline = new Segment[n-1];
        for(int i=0; i<n-1; i++) {
            polyline[i] = new Segment(p[i], p[i + 1]);
            l += polyline[i].length();
        }
        return l;
    }

    public Polyline shift(Point2D a){
        for(int i=0;i<n;i++)
           p[i] = (Point2D)p[i].add(a);
        return new Polyline(p);
    }

    public Polyline rot(double phi){
        for(int i=0;i<n;i++)
            p[i] = (Point2D)p[i].rot(phi);
        return new Polyline(p);
    }

    public Polyline symAxis(int i){
        for(int ind=0;ind<n;ind++)
            p[ind] = (Point2D)p[ind].symAxis(i);
        return new Polyline(p);
    }

    public boolean cross(IShape i){
        if (!(i instanceof Polyline))
            throw new IllegalArgumentException(i + " is not Polyline");

        Segment[] i_polyline = new Segment[((Polyline) i).n-1];
        for(int ind=0; ind<((Polyline) i).n-1; ind++)
            i_polyline[ind] = new Segment(((Polyline) i).p[ind], ((Polyline) i).p[ind + 1]);

        Segment[] this_polyline = new Segment[this.n-1];
        for(int ind=0; ind<this.n-1; ind++)
            this_polyline[ind] = new Segment(this.p[ind], this.p[ind + 1]);

        for(int ind_1=0; ind_1<((Polyline) i).n-1; ind_1++)
            for(int ind_2=0; ind_2<this.n-1; ind_2++)
                if (i_polyline[ind_1].cross(this_polyline[ind_2]))
                    return true;
        return false;
    }

   public String toString(){
        return "n: " + n + ", p: " + p.toString() + '\n';
    }
}

class Circle implements IShape{
    Point2D p;
    double r;

    Circle(Point2D p, double r){
        this.p = p;
        this.r = r;
    }

    Point2D getP(){
        return p;
    }

    void setP(Point2D p){
        this.p = p;
    }

    double getR(){
        return r;
    }

    void setR(double r){
        this.r = r;
    }

    public double square(){
        return Math.PI*r*r;
    }

    public double length(){
        return 2*Math.PI*r;
    }

    public Circle shift(Point2D a){
        return new Circle((Point2D) p.add(a), r);
    }

    public Circle rot(double phi){
        return new Circle((Point2D)p.rot(phi), r);
    }

    public Circle symAxis(int i){
        return new Circle((Point2D)p.symAxis(i), r);
    }

    public boolean cross(IShape i){
        if (!(i instanceof Circle))
            throw new IllegalArgumentException(i + " is not Circle");

        Circle circle_i = (Circle)i;
        double d = Math.sqrt(Math.pow(circle_i.p.x[0]-this.p.x[0], 2) + Math.pow(circle_i.p.x[1]-this.p.x[1], 2));
        if (circle_i.r + this.r >= d)
            return true;
        else
            return false;
    }

    public String toString() {
        return "p: " + p.toString() + ", r:" + r + '\n';
    }
}

class NGon implements IShape, IPolyPoint{
    int n;
    Point2D[] p;

    NGon(Point2D[] p){
        this.p = p;
    }

    int getN(){
        return n;
    }

    Point2D[] getP(){
        return p;
    }

    public Point2D getP(int i){
        return p[i];
    }

    void setP(Point2D[] p){
        this.p = p;
    }

    public void setP(Point2D p, int i){
        this.p[i] = p;
    }

    public double square(){
        double s = 0;
        for(int i=0;i<n-1;i++)
            s += p[i].x[0]*p[i+1].x[1]-p[i].x[1]*p[i+1].x[0];
        return Math.abs(s/2);
    }

    public double length(){
        Polyline polyline = new Polyline(p);
        double len = polyline.length();
        Segment segment = new Segment(p[0], p[n-1]);
        len += segment.length();
        return len;
    }

    public NGon shift(Point2D a){
        Point2D [] p_new = new Point2D[n];
        for(int i=0;i<n;i++)
            p_new[i] = (Point2D)p[i].add(a);
        return new NGon(p_new);
    }

    public NGon rot(double phi){
        Point2D [] p_new = new Point2D[n];
        for(int i=0;i<n;i++)
            p_new[i] = (Point2D)p[i].rot(phi);
        return new NGon(p_new);
    }

    public NGon symAxis(int i){
        Point2D [] p_new = new Point2D[n];
        for(int ind=0;ind<n;ind++)
            p_new[ind] = (Point2D)p[ind].symAxis(ind);
        return new NGon(p_new);
    }

    public boolean cross(IShape i){
        if (!(i instanceof NGon))
            throw new IllegalArgumentException(i + " is not NGon");

        NGon i1 = (NGon)i;
        Segment[] ngon_i = new Segment[i1.n];
        for (int j=0;j<i1.n-1;j++)
            ngon_i[j] = new Segment(i1.p[j], i1.p[j+1]);
        ngon_i[i1.n-1] = new Segment(i1.p[n-1], i1.p[0]);
        Segment[] ngon_this = new Segment[this.n];
        for (int j=0;j<this.n-1;j++)
            ngon_this[j] = new Segment(this.p[j], this.p[j+1]);
        ngon_this[this.n-1] = new Segment(this.p[n-1], this.p[0]);
        for(int j1=0;j1<i1.n;j1++)
            for(int j2=0;j2<this.n;j2++)
                if (ngon_i[j1].cross(ngon_this[j2]))
                    return true;
        return false;
    }

    public String toString(){
        return "n: " + n + ", p: " + p.toString() + '\n';
    }
}

class TGon extends NGon{
    TGon(Point2D[] p){
        super(p);
    }

    public double square(){
        return this.square();
    }
}

//class QGon extends NGon{
//
//}
