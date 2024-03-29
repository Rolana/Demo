/*
 * PointPropertyDialog.java
 *
 * Created on 2004/09/26, 21:18
 */

package org.doubletype.ossa.property;
import org.doubletype.ossa.*;
import org.doubletype.ossa.adapter.*;

/**
 *
 * @author  eugene
 */
public class PointPropertyDialog extends PropertyDialog {
    private EContourPoint m_point;
    
    /** Creates new form PointPropertyDialog */
    public PointPropertyDialog(Desktop a_desktop, PropertyDialog a_next) {
        super(a_desktop, a_next);
        initComponents();
    }
    
    protected boolean isSupport(GlyphObject a_object) {
        return (a_object instanceof EContourPoint);
    }
    
    protected void load(GlyphObject a_object) {
        m_point = (EContourPoint) a_object;
        
        m_edtX.setText(Double.toString(m_point.getX()));
        m_edtY.setText(Double.toString(m_point.getY()));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        m_edtX = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        m_edtY = new javax.swing.JTextField();
        m_btnSave = new javax.swing.JButton();
        m_btnCancel = new javax.swing.JButton();

        jPanel1.setLayout(null);

        jPanel1.setPreferredSize(new java.awt.Dimension(250, 100));
        jLabel1.setText("X:");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(10, 10, 10, 16);

        jPanel1.add(m_edtX);
        m_edtX.setBounds(30, 10, 70, 21);

        jLabel2.setText("Y:");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(10, 40, 41, 16);

        jPanel1.add(m_edtY);
        m_edtY.setBounds(30, 40, 70, 21);

        m_btnSave.setText("Save");
        m_btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_btnSaveActionPerformed(evt);
            }
        });

        jPanel1.add(m_btnSave);
        m_btnSave.setBounds(90, 70, 70, 25);

        m_btnCancel.setText("Cancel");
        m_btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_btnCancelActionPerformed(evt);
            }
        });

        jPanel1.add(m_btnCancel);
        m_btnCancel.setBounds(170, 70, 71, 25);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void m_btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_btnSaveActionPerformed
        exitForm();
    }//GEN-LAST:event_m_btnSaveActionPerformed

    private void m_btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_btnCancelActionPerformed
        exitForm();
    }//GEN-LAST:event_m_btnCancelActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton m_btnCancel;
    private javax.swing.JButton m_btnSave;
    private javax.swing.JTextField m_edtX;
    private javax.swing.JTextField m_edtY;
    // End of variables declaration//GEN-END:variables
    
}
