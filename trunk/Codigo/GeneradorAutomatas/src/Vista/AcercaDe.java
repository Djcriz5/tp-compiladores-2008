/*
 * Ventanas1AboutBox.java
 */

package Vista;

public class AcercaDe extends javax.swing.JDialog {

    public AcercaDe(java.awt.Frame parent) {
        super(parent);
        initComponents();
        getRootPane().setDefaultButton(closeButton);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the  Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        javax.swing.JLabel appTitleLabel = new javax.swing.JLabel();
        javax.swing.JLabel versionLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVersionLabel = new javax.swing.JLabel();
        javax.swing.JLabel vendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel appVendorLabel = new javax.swing.JLabel();
        javax.swing.JLabel appDescLabel = new javax.swing.JLabel();
        javax.swing.JLabel imageLabel = new javax.swing.JLabel();
        javax.swing.JLabel versionLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel appVendorLabel1 = new javax.swing.JLabel();
        javax.swing.JLabel appVendorLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(Vista.AplicacionAnalizadorLexico.class).getContext().getResourceMap(AcercaDe.class);
        setTitle(resourceMap.getString("title")); // NOI18N
        setModal(true);
        setName("aboutBox"); // NOI18N
        setResizable(false);

        closeButton.setLabel(resourceMap.getString("closeButton.label")); // NOI18N
        closeButton.setName("closeButton"); // NOI18N
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeButtonMouseClicked(evt);
            }
        });

        appTitleLabel.setFont(appTitleLabel.getFont().deriveFont(appTitleLabel.getFont().getStyle() | java.awt.Font.BOLD, appTitleLabel.getFont().getSize()+4));
        appTitleLabel.setText(resourceMap.getString("Application.title")); // NOI18N
        appTitleLabel.setName("appTitleLabel"); // NOI18N

        versionLabel.setFont(versionLabel.getFont().deriveFont(versionLabel.getFont().getStyle() | java.awt.Font.BOLD));
        versionLabel.setText(resourceMap.getString("versionLabel.text")); // NOI18N
        versionLabel.setName("versionLabel"); // NOI18N

        appVersionLabel.setText(resourceMap.getString("Application.version")); // NOI18N
        appVersionLabel.setName("appVersionLabel"); // NOI18N

        vendorLabel.setFont(vendorLabel.getFont().deriveFont(vendorLabel.getFont().getStyle() | java.awt.Font.BOLD));
        vendorLabel.setText(resourceMap.getString("vendorLabel.text")); // NOI18N
        vendorLabel.setName("vendorLabel"); // NOI18N

        appVendorLabel.setText(resourceMap.getString("Application.vendor")); // NOI18N
        appVendorLabel.setName("appVendorLabel"); // NOI18N

        appDescLabel.setText(resourceMap.getString("appDescLabel.text")); // NOI18N
        appDescLabel.setName("appDescLabel"); // NOI18N

        imageLabel.setIcon(resourceMap.getIcon("imageLabel.icon")); // NOI18N
        imageLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        imageLabel.setDisabledIcon(resourceMap.getIcon("imageLabel.disabledIcon")); // NOI18N
        imageLabel.setName("imageLabel"); // NOI18N
        imageLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);

        versionLabel1.setFont(versionLabel1.getFont().deriveFont(versionLabel1.getFont().getStyle() | java.awt.Font.BOLD));
        versionLabel1.setText(resourceMap.getString("versionLabel1.text")); // NOI18N
        versionLabel1.setName("versionLabel1"); // NOI18N

        appVendorLabel1.setText(resourceMap.getString("appVendorLabel1.text")); // NOI18N
        appVendorLabel1.setName("appVendorLabel1"); // NOI18N

        appVendorLabel2.setText(resourceMap.getString("appVendorLabel2.text")); // NOI18N
        appVendorLabel2.setName("appVendorLabel2"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(imageLabel)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(versionLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(appVersionLabel))
                                    .addComponent(appDescLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(vendorLabel)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(appVendorLabel1)
                                            .addComponent(appVendorLabel)
                                            .addComponent(appVendorLabel2)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(79, 79, 79)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(versionLabel1)
                                    .addComponent(appTitleLabel))))
                        .addGap(32, 32, 32))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(closeButton)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(appTitleLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addComponent(versionLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(appDescLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(appVersionLabel)
                            .addComponent(versionLabel))
                        .addGap(18, 18, 18)
                        .addComponent(vendorLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(appVendorLabel)
                            .addComponent(appVendorLabel1))
                        .addGap(4, 4, 4)
                        .addComponent(appVendorLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(closeButton)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void closeButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeButtonMouseClicked
    this.setVisible(false);
    //this.dispose();
}//GEN-LAST:event_closeButtonMouseClicked
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    // End of variables declaration//GEN-END:variables
    
}
