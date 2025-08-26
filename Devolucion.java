
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class Devolucion extends JFrame {

    private JComboBox<Integer> comboDenominaciones;
    private JTextField txtExistencia, txtValorDevolver;
    private JButton btnActualizar, btnDevolver;
    private JTable tabla;
    private DefaultTableModel modeloTabla;

    // Denominaciones disponibles
    private final int[] denominaciones = {100000, 50000, 20000, 10000, 5000, 2000, 1000, 500, 200, 100, 50};

    // Existencias (denominación → cantidad disponible)
    private Map<Integer, Integer> existencias = new LinkedHashMap<>();

    public Devolucion() {
        setTitle("Devolución de Dinero");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        

        // Inicializar existencias en 0
        for (int d : denominaciones) {
            existencias.put(d, 0);
        }

        comboDenominaciones = new JComboBox<>();
        for (int d : denominaciones) {
            comboDenominaciones.addItem(d);
        }

        txtExistencia = new JTextField(5);
        btnActualizar = new JButton("Actualizar Existencia");
        txtValorDevolver = new JTextField(10);
        btnDevolver = new JButton("Devolver");

        // Tabla de salida
        modeloTabla = new DefaultTableModel(new Object[]{"Cantidad", "Presentación", "Denominación"}, 0);
        tabla = new JTable(modeloTabla);

        // Agregar componentes
        add(new JLabel("Denominación:"));
        add(comboDenominaciones);
        add(txtExistencia);
        add(btnActualizar);
        add(new JLabel("Valor a Devolver:"));
        add(txtValorDevolver);
        add(btnDevolver);
        add(new JScrollPane(tabla));

        // Acción actualizar existencias
        btnActualizar.addActionListener(e -> {
            try {
                int denom = (int) comboDenominaciones.getSelectedItem();
                int cant = Integer.parseInt(txtExistencia.getText());
                existencias.put(denom, cant);
                JOptionPane.showMessageDialog(this, "Existencia actualizada para " + denom);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un número válido.");
            }
        });

        // Acción devolver
        btnDevolver.addActionListener(e -> {
            try {
                int valor = Integer.parseInt(txtValorDevolver.getText());
                devolverDinero(valor);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un valor válido.");
            }
        });
    }

    // Método para calcular la devolución
    private void devolverDinero(int valor) {
        modeloTabla.setRowCount(0); // limpiar tabla

        for (int denom : denominaciones) {
            int disponible = existencias.get(denom);
            int cantidad = Math.min(valor / denom, disponible);

            if (cantidad > 0) {
                String tipo = denom >= 1000 ? "billete" : "moneda";
                modeloTabla.addRow(new Object[]{cantidad, tipo, denom});

                valor -= cantidad * denom;
                existencias.put(denom, disponible - cantidad);
            }
        }

        if (valor > 0) {
            JOptionPane.showMessageDialog(this, "No se pudo devolver todo el dinero. Faltan " + valor);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Devolucion().setVisible(true);
        });
    }
}
