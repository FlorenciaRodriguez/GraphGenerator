package graph;

import java.awt.Color;
import java.awt.Paint;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.util.ShapeUtilities;

import utils.Bandera;
import utils.TresBanderas;

public class Graph {

	private static int idSujeto;
	private static int nroProtocolo;
	private static int radio;
	private static int[] angulo;
	private static int[] protocolos;
	private static int cantBanderas;
	private static int separacion;
	private static Vector<TresBanderas> banderas;
	private static Vector<Boolean>respuestas;

	public static void main(String[] args) {

		banderas=new Vector<>();
		respuestas=new Vector<>();
		DefaultXYDataset dataset = generarDataSet(
				"C:\\Users\\DellPladema\\Desktop\\Nueva carpeta (2)\\VFinal\\WFFlagCollector\\FlagCollector\\FlagCollectorWorkflowHost\\bin\\Release\\11012017 015938 p.m..txt");
		Paint[] colores = { Color.BLUE, Color.RED, Color.GREEN, Color.ORANGE, Color.YELLOW, Color.BLACK, Color.CYAN,
				Color.DARK_GRAY, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE, Color.PINK };
		JFreeChart chart = ChartFactory.createScatterPlot("Sujeto " + idSujeto, "x", "y", dataset);
		chart.getPlot().setBackgroundPaint(null);
		final XYPlot plot = chart.getXYPlot();
		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);

		for (int i = 0; i < dataset.getSeriesCount(); i++) {
			renderer.setSeriesPaint(i, colores[i]);
			renderer.setSeriesShape(i, ShapeUtilities.createDiamond(2));
		}

		plot.setRenderer(renderer);

		// Mostramos la grafica en pantalla
		ChartFrame frame = new ChartFrame("Sujeto " + idSujeto, chart);

		frame.pack();
		frame.setVisible(true);

		
		//////////////////////////////////////////
		System.out.println("Parametros: ");
		System.out.println("IdSujeto: "+idSujeto);
		System.out.println("Numero de Protocolo: "+nroProtocolo);
		System.out.println("Angulos: "+angulo.toString());
		System.out.println("Radio: "+radio);
		if (nroProtocolo==1)
			System.out.println("Protocolos: "+protocolos.toString());
		else
		{
			System.out.println("Cantidad de Banderas: "+cantBanderas);
			System.out.println("Separacion: "+separacion);
		}
		System.out.println("---------------------------------------------------");
		System.out.println("Resultados");
		for (int i=0;i<angulo.length;i++){
			System.out.println("Prueba "+i);
			System.out.println("Angulo "+angulo[i]);
			System.out.println("Banderas\n"+banderas.elementAt(i).print());
			if (nroProtocolo==1)
				System.out.println("Respuesta: "+respuestas.elementAt(i));
		}
	}

	public static DefaultXYDataset generarDataSet(String path) {

		Vector<XYSeries> output = new Vector<>();
		FileReader f = null;
		try {
			f = new FileReader(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader b = new BufferedReader(f);
		String linea;
		try {
			String unaLinea[] = null;
			linea = b.readLine();
			idSujeto = Integer.parseInt(linea.split(": ")[1]);
			linea = b.readLine();
			nroProtocolo = Integer.parseInt(linea.split(": ")[1]);
			linea = b.readLine();
			linea = b.readLine();
			linea = b.readLine();
			radio = Integer.parseInt(linea.split(": ")[1]);
			linea = b.readLine();
			String[] angulosString = linea.split(": ")[1].split(";");
			angulo = new int[angulosString.length];
			for (int i = 0; i < angulosString.length; i++) {
				angulo[i] = Integer.parseInt(angulosString[i]);
			}
			linea = b.readLine();
			if (nroProtocolo == 1) {
				angulosString = linea.split(": ")[1].split(";");
				protocolos = new int[angulosString.length];
				for (int i = 0; i < angulosString.length; i++) {
					protocolos[i] = Integer.parseInt(angulosString[i]);
				}

			} else {
				cantBanderas = Integer.parseInt(linea.split(": ")[1]);
				linea = b.readLine();
				separacion = Integer.parseInt(linea.split(": ")[1]);
			}
			linea = b.readLine();
			linea = b.readLine();

			int totalBanderas = 0;XYSeries series= null;
			Bandera b1 = null, b2 = null, bMedio;
			while ((linea = b.readLine()) != null) {

				boolean break_ = false;
				series = new XYSeries("Recorrido", false);
				
				
				while (!break_ && (linea = b.readLine()) != null) {
					unaLinea = linea.split(";");
					series.add(Double.parseDouble(unaLinea[1].replace(",", ".")),
							Double.parseDouble(unaLinea[2].replace(",", ".")));
					if (unaLinea[6].equals("CAP")) {

						switch (totalBanderas) {
						case 0:
							b1 = new Bandera(Float.parseFloat(unaLinea[3].replace(",", ".")),
									Float.parseFloat(unaLinea[4].replace(",", ".")),
									Integer.parseInt(unaLinea[5].replace(",", ".")));
							totalBanderas = totalBanderas + 1;
							
							break;
						case 1:
							b2 = new Bandera(Float.parseFloat(unaLinea[3].replace(",", ".")),
									Float.parseFloat(unaLinea[4].replace(",", ".")),
									Integer.parseInt(unaLinea[5].replace(",", ".")));
							totalBanderas = 0;
							break;
						
						default:
							break;
						}
					}
					if ((nroProtocolo==2&&unaLinea[6].equals("SEL"))||unaLinea[6].equals("MOS")){
					
						bMedio = new Bandera(Float.parseFloat(unaLinea[3].replace(",", ".")),
								Float.parseFloat(unaLinea[4].replace(",", ".")),
								Integer.parseInt(unaLinea[5].replace(",", ".")));
						totalBanderas = totalBanderas + 1;
						TresBanderas tres=new TresBanderas(b1, b2, bMedio);
						banderas.addElement(tres);
					}
					if (nroProtocolo==1 && unaLinea[6].equals("SEL"))
						if (unaLinea[5].equals("OK"))
							respuestas.addElement(true);
						else
							if (unaLinea[5].equals("NO"))
								respuestas.addElement(false);
					if ( unaLinea[6].equals("DET")) {
						output.addElement(series);
						break_ = true;
					}

				}

			}output.addElement(series);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			b.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DefaultXYDataset dataset = new DefaultXYDataset();
		int i = 0;
		for (XYSeries element : output) {
			dataset.addSeries("Prueba " + i, element.toArray());
			i++;
		}
		return dataset;
	}

}
