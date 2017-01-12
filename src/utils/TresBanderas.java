package utils;

public class TresBanderas{
	private Bandera b1;
	public Bandera getB1() {
		return b1;
	}
	public Bandera getB2() {
		return b2;
	}
	public Bandera getbMedio() {
		return bMedio;
	}
	private Bandera b2;
	private Bandera bMedio;
	public TresBanderas(Bandera b1, Bandera b2, Bandera bMedio) {
		super();
		this.b1 = b1;
		this.b2 = b2;
		this.bMedio = bMedio;
	}
	public String print(){
	return b1.print()+"\n"+b2.print()+"\n"+bMedio.print();
	}
}