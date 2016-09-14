
package siscafe.view.frontend;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Persistence;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import siscafe.controller.AdictionalElementsController;
import siscafe.controller.CategoryPermitsController;
import siscafe.controller.CitySourceController;
import siscafe.controller.ClientsController;
import siscafe.controller.CommodityCaffeeDeliverController;
import siscafe.controller.ConfiguratorOperationController;
import siscafe.controller.CustomsController;
import siscafe.controller.DailyDonwloadCaffeeController;
import siscafe.controller.DepartamentsController;
import siscafe.controller.FractionPalletCaffeeController;
import siscafe.controller.FullConsultStateCaffeeController;
import siscafe.controller.ItemsServicesController;
import siscafe.controller.ShippingLinesController;
import siscafe.controller.MarkCaffeeController;
import siscafe.controller.NavyAgentController;
import siscafe.controller.NoveltysCaffeeController;
import siscafe.controller.PackingCaffeeController;
import siscafe.controller.PermitsController;
import siscafe.controller.ProfilesController;
import siscafe.controller.RemittancesCaffeeController;
import siscafe.controller.RemittancesInternalOrderController;
import siscafe.controller.ShippersController;
import siscafe.controller.SlotStoreController;
import siscafe.controller.StoresCaffeeController;
import siscafe.controller.TypeContainerController;
import siscafe.controller.TypeUnitsController;
import siscafe.controller.UnitsCaffeeController;
import siscafe.controller.UsersController;
import siscafe.controller.WeighingController;
import siscafe.model.AdictionalElements;
import siscafe.model.CategoryPermits;
import siscafe.model.CitySource;
import siscafe.model.Clients;
import siscafe.model.Customs;
import siscafe.model.Departaments;
import siscafe.model.ItemsServices;
import siscafe.model.ShippingLines;
import siscafe.model.MarkCaffee;
import siscafe.model.NavyAgent;
import siscafe.model.NoveltysCaffee;
import siscafe.model.PackagingCaffee;
import siscafe.model.PackingCaffee;
import siscafe.model.Permits;
import siscafe.model.Profiles;
import siscafe.model.RemittancesCaffee;
import siscafe.model.Shippers;
import siscafe.model.SlotStore;
import siscafe.model.StoresCaffee;
import siscafe.model.TypeContainer;
import siscafe.model.TypeUnits;
import siscafe.model.UnitsCaffee;
import siscafe.model.Users;
import siscafe.model.WeighingDownloadCaffee;
import siscafe.model.WeighingPackagingCaffee;
import siscafe.persistence.CategoryPermitsJpaController;
import siscafe.util.ClockPartFrontendView;
import siscafe.util.ReaderProperties;


/**
 *
 * @author jecheverri
 */
public class Frontend extends javax.swing.JFrame implements MouseMotionListener{

    /**
     * Creates new form main
     */
    public Frontend() {
        initComponents();
        setIcon();
    }
    
    public void setIcon(){
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/siscafe/images/siscafe-favicon.png")));
    }
    
    public void initConfig() {
        this.loginView = new LoginView();
        this.usersView = new UsersView();
        this.usersModel = new Users();
        this.categoryPermitsJpaController = new CategoryPermitsJpaController(Persistence.createEntityManagerFactory( "SISCAFE" ));
        Map<String, Object> emfProperties = categoryPermitsJpaController.getEntityManager().getProperties();
        jLabel5.setText((String) emfProperties.get("javax.persistence.jdbc.url"));
        new Thread(new ClockPartFrontendView(jLabel3)).start();
        Dimension desktopSize = jDesktopPane1.getSize();
        Dimension jInternalFrameSize = loginView.getSize();
        loginView.setLocation((desktopSize.width - jInternalFrameSize.width)/2,
    (desktopSize.height- jInternalFrameSize.height)/2);
        jDesktopPane1.add(loginView);
        loginView.setVisible(true);
        this.userController = new UsersController(this.loginView, this, this.usersView, this.usersModel);
        this.userController.initListener();
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jDesktopPane1.addMouseMotionListener(this);
        jLabel7.setText(new ReaderProperties().getProperties("WEIGHTOPERATION"));
        jLabel10.setText(new ReaderProperties().getProperties("STORE"));
    }
    
    public void initPermitsByProfile(Users userLoged) {
        List<CategoryPermits> listCategoryPermits = this.categoryPermitsJpaController.findCategoryPermitsEntities();
        DefaultTreeModel model = (DefaultTreeModel)jTree2.getModel();
        this.root = (DefaultMutableTreeNode)model.getRoot();
        this.root.removeAllChildren();
        Iterator<CategoryPermits> iterator = listCategoryPermits.iterator();
        while(iterator.hasNext()) {
            this.configPermitByCategory(iterator.next(), userLoged.getProfilesId().getPermitsList(), this.root);
        }
        DefaultMutableTreeNode ndcat = new DefaultMutableTreeNode("Salir");
        this.root.add(ndcat);
        model.reload();
        this.userLoged = userLoged;
        jLabel6.setText(userLoged.getUsername());
    }
    
    private void configPermitByCategory(CategoryPermits categoryPermits, List<Permits> listPermits, DefaultMutableTreeNode root) {
        List<Permits> listOnlyCatPmt = new ArrayList();
        Iterator<Permits> iterator = listPermits.iterator();
        while(iterator.hasNext()) {
            Permits permit = iterator.next();
            if(permit.getCategoryPermitsId().getName().matches(categoryPermits.getName())) {
                listOnlyCatPmt.add(permit);
            }
        }
        if(!listOnlyCatPmt.isEmpty()) {
            DefaultMutableTreeNode ndcat = new DefaultMutableTreeNode(categoryPermits.getName());
            Iterator<Permits> iteratorCatPmt = listOnlyCatPmt.iterator();
            while(iteratorCatPmt.hasNext()) {
                Permits permit = iteratorCatPmt.next();
                DefaultMutableTreeNode ndpmt = new DefaultMutableTreeNode(permit.getName());
                ndcat.add(ndpmt);
            }
            this.root.add(ndcat);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree2 = new javax.swing.JTree();
        jDesktopPane1 = new javax.swing.JDesktopPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jToolBar1 = new javax.swing.JToolBar();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JToolBar.Separator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Frontend");

        jSplitPane1.setDividerLocation(250);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setAutoscrolls(true);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(200, 100));

        jTree2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTree2.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Opciones");
        jTree2.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree2.setVisibleRowCount(40);
        jTree2.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
            public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
                jTree2ValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jTree2);

        jSplitPane1.setLeftComponent(jScrollPane2);

        jDesktopPane1.setBackground(new java.awt.Color(204, 204, 204));
        jDesktopPane1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jDesktopPane1Layout = new javax.swing.GroupLayout(jDesktopPane1);
        jDesktopPane1.setLayout(jDesktopPane1Layout);
        jDesktopPane1Layout.setHorizontalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 907, Short.MAX_VALUE)
        );
        jDesktopPane1Layout.setVerticalGroup(
            jDesktopPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 499, Short.MAX_VALUE)
        );

        jSplitPane1.setRightComponent(jDesktopPane1);

        jSplitPane2.setDividerLocation(700);

        jToolBar1.setFloatable(false);

        jLabel3.setText("2016/01/26");
        jToolBar1.add(jLabel3);
        jToolBar1.add(jSeparator1);

        jLabel1.setText("Usuario: ");
        jToolBar1.add(jLabel1);
        jToolBar1.add(jLabel6);
        jToolBar1.add(jSeparator4);

        jLabel8.setText("Tipo operación: ");
        jToolBar1.add(jLabel8);
        jToolBar1.add(jLabel7);
        jToolBar1.add(jSeparator5);

        jLabel9.setText("Bodega: ");
        jToolBar1.add(jLabel9);
        jToolBar1.add(jLabel10);

        jSplitPane2.setRightComponent(jToolBar1);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jLabel2.setText("log in");
        jToolBar2.add(jLabel2);
        jToolBar2.add(jSeparator2);

        jLabel4.setText("Servidor: ");
        jToolBar2.add(jLabel4);

        jLabel5.setText("gamma.copcsa.com");
        jToolBar2.add(jLabel5);
        jToolBar2.add(jSeparator3);

        jSplitPane2.setLeftComponent(jToolBar2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1165, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane2)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int mX = (int) e.getPoint().getX();
        int mY = (int) e.getPoint().getY();
    }
    
    private void jTree2ValueChanged(javax.swing.event.TreeSelectionEvent evt) {//GEN-FIRST:event_jTree2ValueChanged
        String node = evt.getNewLeadSelectionPath().getLastPathComponent().toString();
        switch (node) {
            case "Pesaje de Cafe":
                weighingDownloadCaffee = new WeighingDownloadCaffee();
                weighingPackagingCaffee = new WeighingPackagingCaffee();
                weighingView = new WeighingView();
                jDesktopPane1.removeAll();
                jDesktopPane1.add(weighingView);
                weighingView.setVisible(true);
                weighingController = new WeighingController(weighingView,weighingDownloadCaffee, weighingPackagingCaffee, this.userLoged);
                weighingController.activeBascule();
                weighingController.initListener();
                jLabel2.setText("Pesaje de Café");
                jTree2.clearSelection();
                break;
            case "Perfiles":
                profile = new Profiles();
                profilesView = new ProfilesView();
                jDesktopPane1.removeAll();
                jDesktopPane1.add(profilesView);
                profilesView.setVisible(true);
                profilesController = new ProfilesController(profile,profilesView);
                profilesController.initListener();
                jLabel2.setText("Perfiles");
                jTree2.clearSelection();
                break;
            case "Permisos":
                permitsModel = new Permits();
                permitsView = new PermitsView();
                jDesktopPane1.removeAll();
                jDesktopPane1.add(permitsView);
                permitsView.setVisible(true);
                permitsController = new PermitsController(permitsModel, permitsView);
                permitsController.initListener();
                jLabel2.setText("Permisos");
                jTree2.clearSelection();
                break;
             case "Elementos Adicionales":
                adictionalElements = new AdictionalElements();
                adictionalElementsView = new AdictionalElementsView();
                jDesktopPane1.removeAll();
                jDesktopPane1.add(adictionalElementsView);
                adictionalElementsView.setVisible(true);
                adictionalElementsController = new AdictionalElementsController(adictionalElementsView, adictionalElements);
                adictionalElementsController.initListener();
                jLabel2.setText("Elementos Adicionales");
                jTree2.clearSelection();
                break;
            case "Sedes":
                departamentsModel = new Departaments();
                departamentsView = new DepartamentsView();
                jDesktopPane1.removeAll();
                jDesktopPane1.add(departamentsView);
                departamentsView.setVisible(true);
                departamentsController = new DepartamentsController(departamentsModel, departamentsView);
                departamentsController.initListener();
                jLabel2.setText("Sedes");
                jTree2.clearSelection();
                break;
            case "Usuarios":
                usersModel = new Users();
                usersView = new UsersView();
                jDesktopPane1.removeAll();
                this.userController = new UsersController(this.loginView, this, this.usersView, this.usersModel);
                this.userController.initListener();
                jDesktopPane1.add(usersView);
                usersView.setVisible(true);
                jLabel2.setText("Usuarios");
                jTree2.clearSelection();
                break;
            case "Clientes":
                clientsModel = new Clients();
                clientsView = new ClientsView();
                jDesktopPane1.removeAll();
                this.clientsController = new ClientsController(clientsModel, clientsView);
                this.clientsController.initListener();
                jDesktopPane1.add(clientsView);
                clientsView.setVisible(true);
                jLabel2.setText("Clientes");
                jTree2.clearSelection();
                break;
            case "Bodegas":
                storesCaffeeModel = new StoresCaffee();
                storesCaffeeView = new StoresCaffeeView();
                jDesktopPane1.removeAll();
                this.storesCaffeeController = new StoresCaffeeController(storesCaffeeModel, storesCaffeeView);
                this.storesCaffeeController.initListener();
                jDesktopPane1.add(storesCaffeeView);
                storesCaffeeView.setVisible(true);
                jLabel2.setText("Bodegas");
                jTree2.clearSelection();
                break;
            case "Slots":
                slotStoreModel = new SlotStore();
                slotStoreView = new SlotStoreView();
                jDesktopPane1.removeAll();
                this.slotStoreController = new SlotStoreController(slotStoreModel, slotStoreView);
                this.slotStoreController.initListener();
                jDesktopPane1.add(slotStoreView);
                slotStoreView.setVisible(true);
                jLabel2.setText("Slots");
                jTree2.clearSelection();
                break;
            case "Tipo unidades":
                typeUnitsModel = new TypeUnits();
                typeUnitsView = new TypeUnitsView();
                jDesktopPane1.removeAll();
                this.typeUnitsController = new TypeUnitsController(typeUnitsModel, typeUnitsView);
                this.typeUnitsController.initListener();
                jDesktopPane1.add(typeUnitsView);
                typeUnitsView.setVisible(true);
                jLabel2.setText("Tipo unidades");
                jTree2.clearSelection();
                break;
            case "Unidades":
                unitsCaffeeModel = new UnitsCaffee();
                unitsCaffeeView = new UnitsCaffeeView();
                jDesktopPane1.removeAll();
                this.unitsCaffeeController = new UnitsCaffeeController(unitsCaffeeModel, unitsCaffeeView);
                this.unitsCaffeeController.initListener();
                jDesktopPane1.add(unitsCaffeeView);
                unitsCaffeeView.setVisible(true);
                jLabel2.setText("Unidades");
                jTree2.clearSelection();
                break;
            case "Marcas":
                markCaffeeModel = new MarkCaffee();
                markCaffeeView = new MarkCaffeeView();
                jDesktopPane1.removeAll();
                this.markCaffeeController = new MarkCaffeeController(markCaffeeModel, markCaffeeView);
                this.markCaffeeController.initListener();
                jDesktopPane1.add(markCaffeeView);
                markCaffeeView.setVisible(true);
                jLabel2.setText("Unidades");
                jTree2.clearSelection();
                break;
            case "Lineas navieras":
                shippingLinesModel = new ShippingLines();
                shippingLinesView = new ShippingLinesView();
                jDesktopPane1.removeAll();
                this.shippingLinesController = new ShippingLinesController(shippingLinesModel, shippingLinesView);
                this.shippingLinesController.initListener();
                jDesktopPane1.add(shippingLinesView);
                shippingLinesView.setVisible(true);
                jLabel2.setText("Lineas navieras");
                jTree2.clearSelection();
                break;
            case "Empresas de transportes":
                shippersModel = new Shippers();
                shippersView = new ShippersView();
                jDesktopPane1.removeAll();
                this.shippersController = new ShippersController(shippersModel, shippersView);
                this.shippersController.initListener();
                jDesktopPane1.add(shippersView);
                shippersView.setVisible(true);
                jLabel2.setText("Empresas de transportes");
                jTree2.clearSelection();
                break;
            case "Aduanas":
                customsModel = new Customs();
                customsView = new CustomsView();
                jDesktopPane1.removeAll();
                this.customsController = new CustomsController(customsModel, customsView);
                this.customsController.initListener();
                jDesktopPane1.add(customsView);
                customsView.setVisible(true);
                jLabel2.setText("Aduanas");
                jTree2.clearSelection();
                break;
            case "Tipo contenedores":
                typeContainerModel = new TypeContainer();
                typeContainerView = new TypeContainerView();
                jDesktopPane1.removeAll();
                this.typeContainerController = new TypeContainerController(typeContainerModel, typeContainerView);
                this.typeContainerController.initListener();
                jDesktopPane1.add(typeContainerView);
                typeContainerView.setVisible(true);
                jLabel2.setText("Tipo contenedores");
                jTree2.clearSelection();
                break;
            case "Ciudad de origen":
                citySourceModel = new CitySource();
                citySourceView = new CitySourceView();
                jDesktopPane1.removeAll();
                this.citySourceController = new CitySourceController(citySourceModel, citySourceView);
                this.citySourceController.initListener();
                jDesktopPane1.add(citySourceView);
                citySourceView.setVisible(true);
                jLabel2.setText("Ciudad origen");
                jTree2.clearSelection();
                System.out.println("asdasd");
                break;
            case "Registro de Mercancia Entrega":
                commodityCaffeeDeliverView = new CommodityCaffeeDeliverView();
                jDesktopPane1.removeAll();
                jDesktopPane1.add(commodityCaffeeDeliverView);
                commodityCaffeeDeliverView.setVisible(true);
                commodityCaffeeDeliverController = new CommodityCaffeeDeliverController(commodityCaffeeDeliverView);
                commodityCaffeeDeliverController.initListener();
                jLabel2.setText("Registro de Mercancia Entrega");
                jTree2.clearSelection();
                break;
            case "Items de servicios":
                itemsServicesModel = new ItemsServices();
                itemsServicesView = new ItemsServicesView();
                jDesktopPane1.removeAll();
                this.itemsServicesController = new ItemsServicesController(itemsServicesModel, itemsServicesView);
                this.itemsServicesController.initListener();
                jDesktopPane1.add(itemsServicesView);
                itemsServicesView.setVisible(true);
                jLabel2.setText("Items de servicios");
                jTree2.clearSelection();
                break;
            case "Novedades":
                noveltysCaffeeModel = new NoveltysCaffee();
                noveltysCaffeeView = new NoveltysCaffeeView();
                jDesktopPane1.removeAll();
                this.noveltysCaffeeController = new NoveltysCaffeeController(noveltysCaffeeModel, noveltysCaffeeView);
                this.noveltysCaffeeController.initListener();
                jDesktopPane1.add(noveltysCaffeeView);
                noveltysCaffeeView.setVisible(true);
                jLabel2.setText("Novedades");
                jTree2.clearSelection();
                break;
            case "Tipo Empaques":
                packingCaffeeModel = new PackingCaffee();
                packingCaffeeView = new PackingCaffeeView();
                jDesktopPane1.removeAll();
                packingCaffeeController = new PackingCaffeeController(packingCaffeeModel, packingCaffeeView);
                packingCaffeeController.initListener();
                jDesktopPane1.add(packingCaffeeView);
                packingCaffeeView.setVisible(true);
                jLabel2.setText("Tipo Empaques");
                jTree2.clearSelection();
                break;
            case "Radicar Cafe":
                remittancesCaffeeModel = new RemittancesCaffee();
                remittancesCaffeeView = new RemittancesCaffeeView();
                jDesktopPane1.removeAll();
                remittancesCaffeeController = new RemittancesCaffeeController(remittancesCaffeeModel, userLoged.getUsername(), remittancesCaffeeView);
                remittancesCaffeeController.initListener();
                jDesktopPane1.add(remittancesCaffeeView);
                remittancesCaffeeView.setVisible(true);
                jLabel2.setText("Radicar Café");
                jTree2.clearSelection();
                break;
            case "Descargue cafe en bodegas":
                dailyDownloadCaffeeView = new DailyDownloadCaffeeView();
                dailyDownloadCaffeeController = new DailyDonwloadCaffeeController(dailyDownloadCaffeeView, userLoged.getUsername());
                jDesktopPane1.removeAll();
                dailyDownloadCaffeeController.initListener();
                jDesktopPane1.add(dailyDownloadCaffeeView);
                dailyDownloadCaffeeView.setVisible(true);
                jLabel2.setText("Reporte - Descargue café en bodega");
                jTree2.clearSelection();
                break;
            case "Fraccion de Pallets":
                fractionPalletCaffeeView = new FractionPalletCaffeeView();
                fractionPalletCaffeeController = new FractionPalletCaffeeController(fractionPalletCaffeeView);
                jDesktopPane1.removeAll();
                fractionPalletCaffeeController.initListener();
                jDesktopPane1.add(fractionPalletCaffeeView);
                fractionPalletCaffeeView.setVisible(true);
                jLabel2.setText("Fraccion de Pallets");
                jTree2.clearSelection();
                break;
            case "Ciudades origenes":
                citySourceModel = new CitySource();
                citySourceView = new CitySourceView();
                jDesktopPane1.removeAll();
                citySourceController = new CitySourceController(citySourceModel, citySourceView);
                citySourceController.initListener();
                jDesktopPane1.add(citySourceView);
                citySourceView.setVisible(true);
                jLabel2.setText("Ciudades origenes");
                jTree2.clearSelection();
                break;
            case "Categoria Permisos":
                categoryPermitsModel = new CategoryPermits();
                categoryPermitsView = new CategoryPermitsView();
                jDesktopPane1.removeAll();
                categoryPermitsController = new CategoryPermitsController(categoryPermitsModel, categoryPermitsView);
                categoryPermitsController.initListener();
                jDesktopPane1.add(categoryPermitsView);
                categoryPermitsView.setVisible(true);
                jLabel2.setText("Categoria Permisos");
                jTree2.clearSelection();
                break;
            case "Consulta Integral de Cafe":
                fullConsultStateCaffeeView = new FullConsultStateCaffeeView();
                jDesktopPane1.removeAll();
                fullConsultStateCaffeeController = new FullConsultStateCaffeeController(fullConsultStateCaffeeView,userLoged.getUsername());
                fullConsultStateCaffeeController.initListener();
                jDesktopPane1.add(fullConsultStateCaffeeView);
                fullConsultStateCaffeeView.setVisible(true);
                jLabel2.setText("Consulta Integral de Cafe");
                jTree2.clearSelection();
                break;
            case "Configuracion":
                configuratorOperationView = new ConfiguratorOperationView();
                configuratorOperationController = new ConfiguratorOperationController(configuratorOperationView);
                jDesktopPane1.removeAll();
                jDesktopPane1.add(configuratorOperationView);
                configuratorOperationView.setVisible(true);
                configuratorOperationController.initListener();
                jLabel2.setText("Configuración");
                jTree2.clearSelection();
                break;
            case "Orden Interna de Entrega":
                packagingCaffee = new PackagingCaffee();
                remittancesInternalOrderView = new RemittancesInternalOrderView();
                jDesktopPane1.removeAll();
                jDesktopPane1.add(remittancesInternalOrderView);
                remittancesInternalOrderView.setVisible(true);
                remittancesInternalOrderController = new RemittancesInternalOrderController(remittancesInternalOrderView, userLoged.getUsername());
                remittancesInternalOrderController.initListener();
                jLabel2.setText("Orden Interna de Entrega");
                jTree2.clearSelection();
                break;
            case "Agentes Navieros":
                navyAgent = new NavyAgent();
                navyAgentView = new NavyAgentView();
                jDesktopPane1.removeAll();
                jDesktopPane1.add(navyAgentView);
                navyAgentView.setVisible(true);
                navyAgentController = new NavyAgentController(navyAgent, navyAgentView);
                navyAgentController.initListener();
                jLabel2.setText("Agentes Navieros");
                jTree2.clearSelection();
                break;
            case "Salir":
                int response = JOptionPane.showConfirmDialog(this, "¿Desea salir del sistema?", "Confirmación", JOptionPane.OK_CANCEL_OPTION);
                if(response == 0) {
                    System.exit(0);
                }
                break;
            default:
                break;
        }
    }//GEN-LAST:event_jTree2ValueChanged

    private AdictionalElementsController adictionalElementsController;
    private AdictionalElementsView adictionalElementsView;
    private AdictionalElements adictionalElements;
    private FractionPalletCaffeeController fractionPalletCaffeeController;
    private FractionPalletCaffeeView fractionPalletCaffeeView;
    private CommodityCaffeeDeliverController commodityCaffeeDeliverController;
    private CommodityCaffeeDeliverView commodityCaffeeDeliverView;
    private PackagingCaffee packagingCaffee;
    private NavyAgentController navyAgentController;
    private NavyAgentView navyAgentView;
    private NavyAgent navyAgent;
    private RemittancesInternalOrderView remittancesInternalOrderView;
    private RemittancesInternalOrderController remittancesInternalOrderController;
    private ConfiguratorOperationController configuratorOperationController;
    private ConfiguratorOperationView configuratorOperationView;
    private FullConsultStateCaffeeView fullConsultStateCaffeeView;
    private FullConsultStateCaffeeController fullConsultStateCaffeeController;
    private DailyDonwloadCaffeeController dailyDownloadCaffeeController;
    private DailyDownloadCaffeeView dailyDownloadCaffeeView;
    private CategoryPermitsController categoryPermitsController;
    private CategoryPermitsView categoryPermitsView;
    private CategoryPermits categoryPermitsModel;
    private WeighingPackagingCaffee weighingPackagingCaffee;
    private WeighingDownloadCaffee weighingDownloadCaffee;
    private PackingCaffeeView packingCaffeeView;
    private PackingCaffeeController packingCaffeeController;
    private PackingCaffee packingCaffeeModel;
    private RemittancesCaffeeView remittancesCaffeeView;
    private RemittancesCaffeeController remittancesCaffeeController;
    private RemittancesCaffee remittancesCaffeeModel;
    private NoveltysCaffeeController noveltysCaffeeController;
    private NoveltysCaffeeView noveltysCaffeeView;
    private NoveltysCaffee noveltysCaffeeModel;
    private ItemsServicesView itemsServicesView;
    private ItemsServicesController itemsServicesController;
    private ItemsServices itemsServicesModel;
    private CitySourceController citySourceController;
    private CitySourceView citySourceView;
    private CitySource citySourceModel;
    private TypeContainer typeContainerModel;
    private TypeContainerController typeContainerController;
    private TypeContainerView typeContainerView;
    private CustomsController customsController;
    private Customs customsModel;
    private CustomsView customsView;
    private ShippersController shippersController;
    private ShippersView shippersView;
    private Shippers shippersModel;
    private ShippingLinesController shippingLinesController;
    private ShippingLinesView shippingLinesView;
    private ShippingLines shippingLinesModel;
    private PortOperatorsView portOperatorsView;
    private MarkCaffeeView markCaffeeView;
    private MarkCaffeeController markCaffeeController;
    private MarkCaffee markCaffeeModel;
    private UnitsCaffeeController unitsCaffeeController;
    private UnitsCaffee unitsCaffeeModel;
    private UnitsCaffeeView unitsCaffeeView;
    private TypeUnitsController typeUnitsController;
    private TypeUnitsView typeUnitsView;
    private TypeUnits typeUnitsModel;
    private SlotStoreView slotStoreView;
    private SlotStoreController slotStoreController;
    private SlotStore slotStoreModel;
    private StoresCaffeeView storesCaffeeView;
    private StoresCaffeeController storesCaffeeController;
    private StoresCaffee storesCaffeeModel;
    private ClientsController clientsController;
    private Clients clientsModel;
    private ClientsView clientsView;
    private UsersView usersView; 
    private UsersController usersController;
    private Users usersModel;
    private DepartamentsController departamentsController;
    private DepartamentsView departamentsView;
    private Departaments departamentsModel;
    private Profiles profile;
    private ProfilesController profilesController;
    private ProfilesView profilesView;
    private PermitsController permitsController;
    private PermitsView permitsView;
    private Permits permitsModel;
    private Users userLoged;
    private WeighingController weighingController;
    private WeighingView weighingView;
    private LoginView loginView;
    private UsersController userController;
    private CategoryPermitsJpaController categoryPermitsJpaController;
    private DefaultMutableTreeNode root;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane jDesktopPane1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTree jTree2;
    // End of variables declaration//GEN-END:variables

}
