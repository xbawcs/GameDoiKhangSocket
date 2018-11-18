/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Component;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;
import model.Player;
import model.User;

/**
 *
 * @author nguye
 */
public class ItemModel extends javax.swing.JPanel implements ListCellRenderer<User> {

    /**
     * Creates new form ItemModel
     */
    public ItemModel() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nickName = new javax.swing.JLabel();
        avatar = new javax.swing.JLabel();
        status = new javax.swing.JLabel();

        setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 255, 255), 1, true));
        setPreferredSize(new java.awt.Dimension(200, 50));

        nickName.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        nickName.setText("Nick Name");

        avatar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        avatar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        avatar.setMaximumSize(new java.awt.Dimension(40, 40));
        avatar.setMinimumSize(new java.awt.Dimension(20, 20));
        avatar.setPreferredSize(new java.awt.Dimension(40, 40));

        status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        status.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        status.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        status.setMaximumSize(new java.awt.Dimension(30, 30));
        status.setMinimumSize(new java.awt.Dimension(20, 20));
        status.setPreferredSize(new java.awt.Dimension(30, 30));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14)
                .addComponent(nickName, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nickName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(status, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(avatar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avatar;
    private javax.swing.JLabel nickName;
    private javax.swing.JLabel status;
    // End of variables declaration//GEN-END:variables

    @Override
    public Component getListCellRendererComponent(JList<? extends User> jlist, User e, int i, boolean bln, boolean bln1) {
        ImageIcon icon = null;

        switch (e.getStatus()) {
            case 0:
                icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("images/offline.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                break;
            case 1:
                icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("images/online.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                break;
            default:
                icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("images/busy.png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                break;
        }

        status.setIcon(icon);
        nickName.setText(e.getNickname());
        avatar.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("images/player2.png")).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT)));

        if (bln) {
            setBackground(jlist.getSelectionBackground());
            setForeground(jlist.getSelectionForeground());
        } else {

            setBackground(jlist.getBackground());
            setForeground(jlist.getForeground());
        }
        return this;
    }
}
