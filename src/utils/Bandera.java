package utils;


public class Bandera{
	private float x;
	private float y;
	private int id;
	public Bandera(float x,float y, int id){
		this.x=x;
		this.y=y;
		this.id=id;
	}
	public double getDistancia(Bandera b){
		return Math.sqrt(Math.pow(this.getX()-b.getX(), 2)+Math.pow(this.getY()-b.getY(), 2));
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	public int getId() {
		return id;
	}
	public String print(){
		return this.id+":("+this.x+";"+this.y+")";
	}
}