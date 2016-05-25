
package siscafe.view.frontend;

import java.awt.Dimension;
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
import siscafe.controller.CategoryPermitsController;
import siscafe.controller.CitySourceController;
import siscafe.controller.ClientsController;
import siscafe.controller.CustomsController;
import siscafe.controller.DepartamentsController;
import siscafe.controller.ItemsServicesController;
import siscafe.controller.ShippingLinesController;
import siscafe.controller.MarkCaffeeController;
import siscafe.controller.NoveltysCaffeeController;
import siscafe.controller.PackingCaffeeController;
import siscafe.controller.PermitsController;
import siscafe.controller.PortOperatorsController;
import siscafe.controller.ProfilesController;
import siscafe.controller.RemittancesCaffeeController;
import siscafe.controller.ShippersController;
import siscafe.controller.SlotStoreController;
import siscafe.controller.StoresCaffeeController;
import siscafe.controller.TypeContainerController;
import siscafe.controller.TypeUnitsController;
import siscafe.controller.UnitsCaffeeController;
import siscafe.controller.UsersController;
import siscafe.controller.WeighingController;
import siscafe.model.CategoryPermits;
import siscafe.model.CitySource;
import siscafe.model.Clients;
import siscafe.model.Customs;
import siscafe.model.Departaments;
import siscafe.model.ItemsServices;
import siscafe.model.ShippingLines;
import siscafe.model.MarkCaffee;
import siscafe.model.NoveltysCaffee;
import siscafe.model.PackingCaffee;
import siscafe.model.Permits;
import siscafe.model.PortOperators;
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

        jSplitPane2.setDividerLocation(1000);

        jToolBar1.setFloatable(false);

        jLabel3.setText("2016/01/26");
        jToolBar1.add(jLabel3);
        jToolBar1.add(jSeparator1);

        jLabel1.setText("usuario: ");
        jToolBar1.add(jLabel1);
        jToolBar1.add(jLabel6);

        jSplitPane2.setRightComponent(jToolBar1);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jLabel2.setText("log in");
        jToolBar2.add(jLabel2);
        jToolBar2.add(jSeparator2);

        jLabel4.setText("servidor: ");
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
            case "Pesaje de Café":
                weighingDownloadCaffee = new WeighingDownloadCaffee();
                weighingView = new WeighingView();
                jDesktopPane1.removeAll();
                jDesktopPane1.add(weighingView);
                weighingView.setVisible(true);
                weighingController = new WeighingController(weighingView,weighingDownloadCaffee, this.userLoged);
                weighingController.activeBascule();
                weighingController.initListener();
                jLabel2.setText("Pesaje de Café");
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
                break;
            case "Operadores Portuarios":
                portOperatorsModel = new PortOperators();
                portOperatorsView = new PortOperatorsView();
                jDesktopPane1.removeAll();
                this.portOperatorsController = new PortOperatorsController(portOperatorsModel, portOperatorsView);
                this.portOperatorsController.initListener();
                jDesktopPane1.add(portOperatorsView);
                portOperatorsView.setVisible(true);
                jLabel2.setText("Operadores Portuarios");
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
                break;
            case "Ciudad origen":
                citySourceModel = new CitySource();
                citySourceView = new CitySourceView();
                jDesktopPane1.removeAll();
                this.citySourceController = new CitySourceController(citySourceModel, citySourceView);
                this.typeContainerController.initListener();
                jDesktopPane1.add(citySourceView);
                citySourceView.setVisible(true);
                jLabel2.setText("Ciudad origen");
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
                break;
            case "Radicar Café":
                remittancesCaffeeModel = new RemittancesCaffee();
                remittancesCaffeeView = new RemittancesCaffeeView();
                pDFPanelViewerView = new PDFPanelViewerView();
                jDesktopPane1.removeAll();
                remittancesCaffeeController = new RemittancesCaffeeController(remittancesCaffeeModel, pDFPanelViewerView, remittancesCaffeeView);
                remittancesCaffeeController.initListener();
                jDesktopPane1.add(remittancesCaffeeView);
                remittancesCaffeeView.setVisible(true);
                jLabel2.setText("Radicar Café");
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

    private CategoryPermitsController categoryPermitsController;
    private CategoryPermitsView categoryPermitsView;
    private CategoryPermits categoryPermitsModel;
    private WeighingDownloadCaffee weighingDownloadCaffee;
    private PackingCaffeeView packingCaffeeView;
    private PackingCaffeeController packingCaffeeController;
    private PackingCaffee packingCaffeeModel;
    private RemittancesCaffeeView remittancesCaffeeView;
    private RemittancesCaffeeController remittancesCaffeeController;
    private RemittancesCaffee remittancesCaffeeModel;
    private PDFPanelViewerView pDFPanelViewerView;
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
    private PortOperatorsController portOperatorsController; 
    private PortOperators portOperatorsModel;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JTree jTree2;
    // End of variables declaration//GEN-END:variables

}
