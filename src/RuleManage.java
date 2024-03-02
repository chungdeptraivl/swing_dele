import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class RuleManage {
    private JFrame frame;
    private JTextField txtMaQuyen;
    private JTextField txtTenQuyen;
    private JComboBox<String> cboNhomQuyen;
    private JTable table;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    String[] nhomQuyenValues = {"Chọn nhóm", "Quản trị", "Kế toán", "Kho vận", "Bán hàng"};

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                RuleManage window = new RuleManage();
                window.frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public RuleManage() {
        connect();
        initialize();
        loadTable();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/ql_quyen?characterEncoding=utf8";
            String username = "root";
            String password = "";
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private void clear() {
        txtMaQuyen.setText("");
        txtTenQuyen.setText("");
        cboNhomQuyen.setSelectedIndex(0);
    }

    private void loadTable() {
        try {
            if (table != null) {
                pst = con.prepareStatement("SELECT * FROM quyen");
                rs = pst.executeQuery();
                table.setModel(buildTableModel(rs));
            } else {
                System.out.println("Biến table chưa được khởi tạo.");
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
    }

    private void initialize() {
        // Frame body
        frame = new JFrame();
        frame.setTitle("Quản lý quyền");
        frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
        frame.getContentPane().setLayout(null);
        frame.setSize(800, 700);
        frame.setResizable(false);

        int frameWidth = frame.getWidth();

        // Header
        JLabel lbHeader = new JLabel("Quản lý quyền");
        lbHeader.setForeground(new Color(3, 81, 148));
        lbHeader.setFont(new Font("Tahoma", Font.BOLD, 18));
        int headerWidth = lbHeader.getPreferredSize().width;
        int headerHeight = lbHeader.getPreferredSize().height;
        int x = (frameWidth - headerWidth) / 2;
        lbHeader.setBounds(x, 10, headerWidth, headerHeight);
        frame.getContentPane().add(lbHeader);

        // Panel form
        JPanel plForm = new JPanel();
        plForm.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        int formWidth = 700;
        int formHeight = 200;
        int x2 = (frameWidth - formWidth) / 2;
        plForm.setBounds(x2, 40, formWidth, formHeight);
        plForm.setLayout(null);
        frame.getContentPane().add(plForm);

        // Form
        JLabel lbMaSP = new JLabel("Mã quyền:");
        lbMaSP.setFont(new Font("Tahoma", Font.PLAIN, 14));
        Dimension lbMaSPSize = lbMaSP.getPreferredSize();
        lbMaSP.setBounds(21, 46, lbMaSPSize.width, lbMaSPSize.height);
        plForm.add(lbMaSP);

        JLabel lbTenSP = new JLabel("Tên quyền:");
        lbTenSP.setFont(new Font("Tahoma", Font.PLAIN, 14));
        Dimension lbTenSPSize = lbTenSP.getPreferredSize();
        lbTenSP.setBounds(21, 81, lbTenSPSize.width, lbTenSPSize.height);
        plForm.add(lbTenSP);

        JLabel lbGiaSP = new JLabel("Nhóm quyền:");
        lbGiaSP.setFont(new Font("Tahoma", Font.PLAIN, 14));
        Dimension lbGiaSPSize = lbGiaSP.getPreferredSize();
        lbGiaSP.setBounds(21, 116, lbGiaSPSize.width, lbGiaSPSize.height);
        plForm.add(lbGiaSP);

        int txtMaY = lbMaSP.getY();
        txtMaQuyen = new JTextField();
        txtMaQuyen.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtMaQuyen.setBounds(121, txtMaY, 500, 20);
        txtMaQuyen.setColumns(10);
        plForm.add(txtMaQuyen);

        int txtTenY = lbTenSP.getY();
        txtTenQuyen = new JTextField();
        txtTenQuyen.setFont(new Font("Tahoma", Font.PLAIN, 14));
        txtTenQuyen.setBounds(121, txtTenY, 500, 20);
        txtTenQuyen.setColumns(10);
        plForm.add(txtTenQuyen);

        int txtGiaY = lbGiaSP.getY();
        cboNhomQuyen = new JComboBox<>(nhomQuyenValues);
        cboNhomQuyen.setFont(new Font("Tahoma", Font.PLAIN, 14));
        cboNhomQuyen.setBounds(121, txtGiaY, 500, 20);
        cboNhomQuyen.setSelectedIndex(0);
        plForm.add(cboNhomQuyen);

        // Btn them
        JButton btnSave = new JButton("Thêm");
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cboNhomQuyen.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(null, "Vui lòng chọn nhóm quyền!");
                    return;
                }
                saveData();
            }
        });
        btnSave.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnSave.setBounds(150, txtGiaY + 50, 89, 23);
        plForm.add(btnSave);

        // Btn sua
        int btnUpdateX = btnSave.getX();
        JButton btnUpdate = new JButton("Sửa");
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateData();
            }
        });
        btnUpdate.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnUpdate.setBounds(btnUpdateX + 100, txtGiaY + 50, 89, 23);
        plForm.add(btnUpdate);

        // Btn xoa
        int btnDeleteX = btnUpdate.getX();
        JButton btnDelete = new JButton("Xoá");
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteData();
            }
        });
        btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnDelete.setBounds(btnDeleteX + 100, txtGiaY + 50, 89, 23);
        plForm.add(btnDelete);

        // Btn tim kiem
        int btnSearchX = btnDelete.getX();
        JButton btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchData();
            }
        });
        btnSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnSearch.setBounds(btnSearchX + 100, txtGiaY + 50, 89, 23);
        plForm.add(btnSearch);

        // Btn tim kiem
        int btnLoadX = btnSearch.getX();
        JButton btnLoad = new JButton("Load bảng");
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadTable();
            }
        });
        btnLoad.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btnLoad.setBounds(btnLoadX + 100, txtGiaY + 50, 89, 23);
        plForm.add(btnLoad);

        //Table
        JScrollPane slpTable = new JScrollPane();
        int slpTableWidth = 700;
        int slpTableHeight = 350;
        int x3 = (frameWidth - slpTableWidth) / 2;
        slpTable.setBounds(x3, 250, slpTableWidth, slpTableHeight);
        slpTable.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        frame.getContentPane().add(slpTable);

        table = new JTable();
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int index = table.getSelectedRow();
                    TableModel model = table.getModel();
                    txtMaQuyen.setText(model.getValueAt(index, 0).toString());
                    txtTenQuyen.setText(model.getValueAt(index, 1).toString());
                    cboNhomQuyen.setSelectedItem(model.getValueAt(index, 2).toString());
                }
            }
        });
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setForeground(new Color(8, 48, 148));
        table.setGridColor(new Color(47, 47, 47));
        table.getTableHeader().setPreferredSize(new Dimension(table.getColumnModel().getTotalColumnWidth(), 40));
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 18));
        table.getTableHeader().setBackground(new Color(197, 228, 243));
        table.setDefaultEditor(Object.class, null);
        slpTable.setViewportView(table);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        DefaultTableModel model = new DefaultTableModel();
        int columnCount = rs.getMetaData().getColumnCount();

        model.addColumn("Mã quyền");
        model.addColumn("Tên quyền");
        model.addColumn("Nhóm quyền");

        while (rs.next()) {
            Object[] row = new Object[columnCount];

            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = rs.getObject(i);
            }
            model.addRow(row);
        }

        rs.close();

        return model;
    }

    // HAM CRUD
    // save data function
    private void saveData() {
        String ma = txtMaQuyen.getText();
        String ten = txtTenQuyen.getText();
        String nhom = (String) cboNhomQuyen.getSelectedItem();

        if (ma.isEmpty() || ten.isEmpty() || nhom.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Điền đầy đủ thông tin !!!");
            return;
        }

        try {
            Rule newRule = new Rule(ma, ten, nhom);
            String sql = "INSERT INTO quyen (ma_quyen, ten_quyen, nhom_quyen) VALUE (?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setString(1, newRule.getMa());
            pst.setString(2, newRule.getTen());
            pst.setString(3, newRule.getNhom());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm quyền thành công");
            clear();
            loadTable();
        } catch (SQLException e3) {
            e3.printStackTrace();
        }
    }

    // update data function
    private void updateData() {
        String ma = txtMaQuyen.getText();
        String ten = txtTenQuyen.getText();
        String nhom = (String) cboNhomQuyen.getSelectedItem();

        if (ma.isEmpty() || ten.isEmpty() || nhom.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Chọn một quyền từ bảng !!!");
            return;
        }

        try {
            Rule updateRule = new Rule(ma, ten, nhom);
            String sql = "UPDATE quyen SET ten_quyen = ?, nhom_quyen = ? WHERE ma_quyen = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, updateRule.getTen());
            pst.setString(2, updateRule.getNhom());
            pst.setString(3, updateRule.getMa());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cập nhật quyền thành công");
            clear();
            loadTable();
        } catch (SQLException e4) {
            e4.printStackTrace();
        }
    }

    // delete data function
    private void deleteData() {
        String ma = txtMaQuyen.getText();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Chọn một quyền từ bảng !!!");
            return;
        }

        int result = JOptionPane.showConfirmDialog(null, "Bạn có muốn xoá quyền ?", "Xoá quyền", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM quyen WHERE ma_quyen = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, ma);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(null, "Xoá quyền thành công");
                clear();
                loadTable();
            } catch (SQLException e6) {
                e6.printStackTrace();
            }
        }
    }

    // search data function
    private void searchData() {
        String ma = txtMaQuyen.getText();

        if (ma.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Chọn một quyền từ bảng !!!");
            return;
        }

        try {
            String sql = "SELECT * FROM quyen WHERE ma_quyen = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, ma);
            rs = pst.executeQuery();

            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "Không tìm thấy quyền");
            } else {
                table.setModel(buildTableModel(rs));
            }
        } catch (SQLException e7) {
            e7.printStackTrace();
        }
    }
}
