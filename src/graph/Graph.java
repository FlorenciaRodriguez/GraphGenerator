package graph;

import java.awt.Color;
import java.awt.Paint;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JFileChooser;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
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
	private static Vector<Boolean> respuestas;
	private static Vector<Integer> testConfianza;
	private static Vector<String> lineas;

	public static void main(String[] args) {
		lineas = new Vector<>();
		banderas = new Vector<TresBanderas>();
		respuestas = new Vector<Boolean>();
		testConfianza = new Vector<Integer>();
		String path;
		if (args.length == 0) {
			final JFileChooser fc = new JFileChooser();
			fc.showOpenDialog(null);
			path = fc.getSelectedFile().getAbsolutePath();
		} else {
			path = args[0];
		}
		System.out.println(path);
		DefaultXYDataset dataset = generarDataSet(path);
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

		frame.setVisible(true);
		frame.pack();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}

		try {
			File filename_png = new File(path.substring(0, path.length() - 4) + "_GRAPH.png");
			filename_png.createNewFile();
			ChartUtilities.saveChartAsPNG(filename_png, chart, 1024, 800);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//////////////////////////////////////////
		lineas.addElement("Parametros: ");
		System.out.println("Parametros: ");
		lineas.addElement("IdSujeto: " + idSujeto);
		System.out.println("IdSujeto: " + idSujeto);
		lineas.addElement("Numero de Protocolo: " + nroProtocolo);
		System.out.println("Numero de Protocolo: " + nroProtocolo);
		lineas.addElement("Radio: " + radio);
		System.out.println("Radio: " + radio);
		if (nroProtocolo != 1) {
			lineas.addElement("Cantidad de Banderas: " + cantBanderas);
			System.out.println("Cantidad de Banderas: " + cantBanderas);
			lineas.addElement("Separacion: " + separacion);
			System.out.println("Separacion: " + separacion);
		}
		System.out.println("Resultados");
		lineas.addElement("Resultados");
		if (nroProtocolo != 0)
			for (int i = 0; i < angulo.length; i++) {
				lineas.addElement("Prueba: " + i);
				System.out.println("Prueba: " + i);
				if (nroProtocolo == 2)
					lineas.addElement("Angulo: " + angulo[i]);
				System.out.println("Angulo: " + angulo[i]);
				lineas.addElement("Banderas\n" + banderas.elementAt(i).print());
				System.out.println("Banderas\n" + banderas.elementAt(i).print());
				if (nroProtocolo == 1) {
					lineas.addElement("Protocolo: " + protocolos[i]);
					System.out.println("Protocolo: " + protocolos[i]);
					lineas.addElement("Respuesta: " + respuestas.elementAt(i));
					System.out.println("Respuesta: " + respuestas.elementAt(i));
				}

				if (!testConfianza.isEmpty()) {
					lineas.addElement("Confianza: " + testConfianza.elementAt(i));
					System.out.println("Confianza: " + testConfianza.elementAt(i));
				}
			}

		String sFichero = path.substring(0, path.length() - 4) + "_RES.txt";
		File fichero = new File(sFichero);

		if (!fichero.exists()) {
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(sFichero));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String s : lineas)
				try {
					bw.write(s + "\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private static String angulos;
	private static String protocolos_;

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
			/**** Lectura de parametros ****/
			String unaLinea[] = null;
			linea = b.readLine();
			idSujeto = Integer.parseInt(linea.split(": ")[1]);
			linea = b.readLine();
			nroProtocolo = Integer.parseInt(linea.split(": ")[1]);
			linea = b.readLine();
			radio = Integer.parseInt(linea.split(": ")[1]);
			String[] angulosString;
			if (nroProtocolo == 1 || nroProtocolo == 2) {
				linea = b.readLine();
				angulos = linea.split(": ")[1];
				angulosString = angulos.split(",");
				angulo = new int[angulosString.length];
				for (int i = 0; i < angulosString.length; i++) {
					angulo[i] = Integer.parseInt(angulosString[i]);
				}
			}

			linea = b.readLine();
			if (nroProtocolo == 1) {
				protocolos_ = linea.split(": ")[1];
				angulosString = protocolos_.split(",");
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
			/*******/

			/*** Construccion del dataset **/
			int totalBanderas = 0;
			XYSeries series = null;
			String confianza = "NULL";
			Bandera b1 = null, b2 = null, bMedio = null;

			if ((nroProtocolo != 0))
				while (((linea = b.readLine()) != null)) {

					boolean break_ = false;
					series = new XYSeries("Recorrido", false);

					// Una prueba
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
						if ((nroProtocolo == 2 && unaLinea[6].equals("SEL")) || (unaLinea[6].equals("MOS"))) {
							bMedio = new Bandera(Float.parseFloat(unaLinea[3].replace(",", ".")),
									Float.parseFloat(unaLinea[4].replace(",", ".")),
									Integer.parseInt(unaLinea[5].replace(",", ".")));
							totalBanderas = totalBanderas + 1;
							TresBanderas tres = new TresBanderas(b1, b2, bMedio);
							banderas.addElement(tres);
						}
						if (nroProtocolo == 1 && unaLinea[6].equals("SEL"))
							if (unaLinea[5].equals("OK"))
								respuestas.addElement(true);
							else if (unaLinea[5].equals("NO"))
								respuestas.addElement(false);
						if (unaLinea[6].equals("RES")) {
							confianza = unaLinea[5];

						}
						if ((unaLinea[6].equals("DET") || unaLinea[6].equals("FIN"))) {
							output.addElement(series);
							break_ = true;
							totalBanderas = 0;
							if (!confianza.equals("NULL")) {

								testConfianza.addElement(Integer.parseInt(confianza));
								confianza = "NULL";
							}

						}
					}

				}
			else {
				series = new XYSeries("Recorrido", false);
				while (((linea = b.readLine()) != null)) {
					unaLinea = linea.split(";");
					series.add(Double.parseDouble(unaLinea[1].replace(",", ".")),
							Double.parseDouble(unaLinea[2].replace(",", ".")));
				}
				output.addElement(series);
			}

			// output.addElement(series);
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
