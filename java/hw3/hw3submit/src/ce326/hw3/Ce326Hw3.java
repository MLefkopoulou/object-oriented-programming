/*
> NAME : Lefkopoulou Eleni Maria
> AEM  : 2557
> HW   : 3 - File Browser
> DATE : 24/04/2020
> Class: Ce326Hw3 is the class that contains all the functions that are needed
*/
package ce326.hw3;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class Ce326Hw3 {
    JFrame frame ;
    JMenuBar bar ;
    //edit menu
    JMenu editMenu;
    JMenuItem cut;
    JMenuItem copy;
    JMenuItem paste;
    JMenuItem rename;
    JMenuItem delete;
    JMenuItem favourites;
    JMenuItem properties;
    //Panels
    JPanel searchPanel;
    JPanel content;
    JPanel breadcrumbPanel ;
    JPanel upPanel;
    //INFO FOR SELECT
    JLabel isSelected;
    breadcrumbNode isSelectedNode=null;
    File isSelectedFile=null;
    int selected =0;
    breadcrumbNode root = null;
    //INFO FOR COPY
    File copyFile;
    int copySignal = 0;
    int stopcopypaste =0;
    //INFO FOR CUT
    File cutFile;
    int cutSignal = 0;
    //INFO FOR SEARCH
    JList<String> searchlist;
    DefaultListModel<String> listModel;
    JScrollPane listScrollPane;
    JTextField typeForSearch;
    int searchResults =0;
    //INFO FOR FAV
    JPanel favouritesPanel;
    java.util.List<JLabel> FavouritesList = new ArrayList<>();
    int favouritesSize =0;
    JLabel favtext;
    //INFO for content directory
    java.util.List<JLabel> directoryLabels = new ArrayList<>();
    int directories =0;
    //INFO for content files
    java.util.List<JLabel> fileLabels = new ArrayList<>();
    int files = 0;
    //INFO for breadcrumb
    java.util.List<breadcrumbNode> listOfNodes = new ArrayList<>();
    java.util.List<JLabel> BreadcrumbList = new ArrayList<>();
    int sizeOfBreadcrumb=0;
    int sizeOfBreadcrumbPath=0;

    ////////////  Constructor  //////////
    public Ce326Hw3(){
        frame = new JFrame("Ce326Hw3");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        MakeMenu();
        addPanel();
        frame.setVisible(true);
    }
    ////////////  Make Menu  //////////
    public void MakeMenu(){
        //File
        JMenu fileMenu = new JMenu("File");
        //File MenuItems
        //New Window//
        JMenuItem newWindow = new JMenuItem("New Window");
        newWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                      Ce326Hw3 newbrowser  = new Ce326Hw3();
                    }
                  });

            }
        });
        fileMenu.add(newWindow);
        //Exit//
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
            }
        });
        fileMenu.add(exit);
        //Edit
        editMenu = new JMenu("Edit");
        //Edit MenuItems
        //Cut//
        cut = new JMenuItem("Cut");
        cut.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              if(copySignal ==1 ){
                  copySignal =0;
                  copyFile = null;
              }
              cutSignal = 1;
              cutFile = new File(isSelectedFile.getAbsolutePath());
              //enableMenu('p');
            }
        });
        cut.setEnabled(false);
        editMenu.add(cut);
        //Copy//
        copy = new JMenuItem("Copy");
        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              if(cutSignal ==1 ){
                  cutSignal =0;
                  cutFile = null;
              }
              copySignal = 1;
              copyFile = new File(isSelectedFile.getAbsolutePath());
              //enableMenu('p');
            }
            });
        copy.setEnabled(false);
        editMenu.add(copy);
        //Paste//
        paste = new JMenuItem("Paste");
        paste.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    if(cutSignal ==1)cutpaste(cutFile,new File(isSelectedFile.getAbsolutePath()+root.characteristic+cutFile.getName()));
                    else
                    copyPasteFunction(copyFile,new File(isSelectedFile.getAbsolutePath()+root.characteristic+copyFile.getName()));

            }
        });
        paste.setEnabled(false);
        editMenu.add(paste);
        //Rename//
        rename = new JMenuItem("Rename");
        rename.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

              renameFunction(isSelectedFile,isSelectedNode.listOfFiles);
            }
        });
        rename.setEnabled(false);
        editMenu.add(rename);
        //Delete//
        delete = new JMenuItem("Delete");
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

              deleteFunction(isSelectedFile,isSelectedNode.listOfFiles);
            }
        });
        delete.setEnabled(false);
        editMenu.add(delete);
        //Add to Favourites//
        favourites = new JMenuItem("Add to Favourites");
        favourites.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean check;
                check = addToFavourites(isSelectedFile.getName(),isSelectedFile.getAbsolutePath());
                if(check == false){
                    JOptionPane.showMessageDialog(frame, "This directory is already added at favourites", "Inane error",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        favourites.setEnabled(false);
        editMenu.add(favourites);
        //Properties//
        properties = new JMenuItem("Properties");
        properties.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              if(selected == 0){
                  propertiesFunction(new File(listOfNodes.get(sizeOfBreadcrumb-1).fullPath));
              }
              else{
                propertiesFunction(isSelectedFile);
              }
            }
        });
        properties.setEnabled(true);
        editMenu.add(properties);

        //Search
        JMenuItem searchMenu = new JMenuItem("Search");
        searchMenu.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            if (searchPanel.isVisible()){
                searchPanel.setVisible(false);
                if(searchResults==1){
                    upPanel.remove(listScrollPane);
                    upPanel.updateUI();
                }
                typeForSearch.setText("");
             }
             else{
                searchPanel.setVisible(true);
             }

            }
        });
        //Menu Bar
        bar = new JMenuBar();
        bar.add(fileMenu);
        bar.add(editMenu);
        bar.add(searchMenu);
        Border barline = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        bar.setBorder(barline);
        frame.setJMenuBar(bar);
    }
    ////////// Add panel //////////
    public void addPanel(){
        Container mainPanel = frame.getContentPane();
        mainPanel.setLayout(new BorderLayout());
        //Panel for favourites
        favouritesPanel = new JPanel();
        Border  grey= BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        favouritesPanel.setBorder(grey);
        favouritesPanel.setBackground(Color.WHITE);
        favouritesPanel.setLayout(new BoxLayout(favouritesPanel,BoxLayout.Y_AXIS));
        String userHome = "user.home";
        String path = System.getProperty(userHome);
        File favfile = new File(path);
        addToFavourites(favfile.getName(),path);
        favtext =new JLabel("Favourites directories\n");
        favouritesPanel.add(favtext);
        setFavourites();
        mainPanel.add(favouritesPanel,BorderLayout.WEST);
        //Panel right
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        //Up panel
        upPanel = new JPanel();
        upPanel.setLayout(new BoxLayout(upPanel, BoxLayout.Y_AXIS));
        //Panel search
        searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        typeForSearch = new JTextField("", 30);
        typeForSearch.setColumns(1);
        Border searchline = BorderFactory.createLineBorder(Color.black);
        typeForSearch.setBorder(searchline);
        searchPanel.add(typeForSearch);
        JButton searchBotton = new JButton();
        searchBotton.setText("Search");
        searchBotton.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e){
                   if(searchResults==1){
                        upPanel.remove(listScrollPane);
                        upPanel.updateUI();

                  }
                   listModel = new DefaultListModel<String>();
                   String input = typeForSearch.getText();
                   if(!"".equals(input)){
                   int index= input.indexOf("type:");
                   String name,type;
                   if(index == -1){
                       name = input;
                       name =name.toLowerCase();
                       type =null;
                   }
                   else{
                       name = input.substring(0, index-1);
                       name =name.toLowerCase();
                       type = input.substring(index+5);

                       type = type.toLowerCase();
                   }

                   searchFunction(name,type,new File(listOfNodes.get(listOfNodes.size()-1).fullPath));
                   if(listModel.isEmpty()){
                        JOptionPane.showMessageDialog(frame, "NO RESULT", "Inane error",JOptionPane.INFORMATION_MESSAGE);
                        typeForSearch.setText("");
                        upPanel.updateUI();
                   }
                   else{
                    searchlist = new JList<>(listModel);
                    searchlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    if(listModel.size() >=5){
                    searchlist.setVisibleRowCount(5);
                    }
                    else{
                      searchlist.setVisibleRowCount(listModel.size());
                    }
                    searchlist.setLayoutOrientation(JList.VERTICAL);
                    searchlist.addListSelectionListener(new ListSelectionListener(){public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting()) {
                         int position = searchlist.getSelectedIndex();
                         String path = listModel.get(position);
                         File SearchFile = new File(path);
                         if(SearchFile.isDirectory()){
                             setNewBreadcrumb(SearchFile.getAbsolutePath());
                                removeContent();
                                addIcons((ArrayList<File>) listOfNodes.get(listOfNodes.size()-1).listOfFiles);
                         }
                         else{
                            if(SearchFile.canExecute()){
                                try {
                                    Runtime.getRuntime().exec(SearchFile.getAbsolutePath(), null, SearchFile.getParentFile());
                                } catch (IOException ex) {
                                    Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                                }


                           }
                           else{
                                if(!Desktop.isDesktopSupported()){
                                  System.out.println("Desktop is not supported");
                                  return;
                               }

                               Desktop desktop = Desktop.getDesktop();
                               if(SearchFile.exists()) try {
                                   desktop.open(SearchFile);
                               } catch (IOException ex) {
                                   Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                               }
                           }
                   }
                            upPanel.remove(listScrollPane);
                            typeForSearch.setText("");
                            upPanel.updateUI();

                        }
                        }
                    });
                    listScrollPane = new JScrollPane(searchlist);
                    listScrollPane.setVisible(true);
                    upPanel.add(listScrollPane);
                    searchResults =1;
                    upPanel.updateUI();
                   }
                }
                else{
                     JOptionPane.showMessageDialog(frame, "no input for search", "Inane error",JOptionPane.ERROR_MESSAGE);

                }
        }
            });
        searchPanel.add(searchBotton);
        searchPanel.setVisible(false);
        upPanel.add(searchPanel);
        //breadcrumb
        breadcrumbPanel = new JPanel();
        breadcrumbPanel.setLayout(new FlowLayout(BoxLayout.X_AXIS));
        setNewBreadcrumb(path);
        Border breadcrumbline = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        breadcrumbPanel.setBorder(breadcrumbline);
        JScrollPane scrollbreadcrumb = new JScrollPane(breadcrumbPanel);
        scrollbreadcrumb.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollbreadcrumb.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollbreadcrumb.setPreferredSize(new Dimension(900,40));
        upPanel.add(scrollbreadcrumb);
        rightPanel.add(upPanel,BorderLayout.NORTH);
        //Panel for content
        content = new JPanel();

        Border contentline = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
        content.setBorder(contentline);
        content.setLayout(new WrapLayout(FlowLayout.LEFT));//FlowLayout.LEFT
        content.setBackground(Color.WHITE);
        addIcons((ArrayList<File>) listOfNodes.get(listOfNodes.size()-1).listOfFiles);

        JScrollPane scrollPane = new JScrollPane(content,   ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        rightPanel.add(scrollPane,BorderLayout.CENTER);
        mainPanel.add(rightPanel,BorderLayout.CENTER);

    }
    ////////// Add Link //////////
    public void addLink(String name,JPanel panel,int here){
        String nameOfLink = name;
        JLabel link = new JLabel(nameOfLink);
        link.setForeground(Color.BLUE.darker());
        link.setCursor(new Cursor(Cursor.HAND_CURSOR));
        if(listOfNodes.size() > 1){
            JLabel breadcrumb = new JLabel(" > ");
            breadcrumb.setForeground(Color.BLUE.darker());
            BreadcrumbList.add(sizeOfBreadcrumbPath,breadcrumb);
            sizeOfBreadcrumbPath++;
            panel.add(breadcrumb);

        }
        BreadcrumbList.add(sizeOfBreadcrumbPath,link);
        sizeOfBreadcrumbPath++;
        int removeInt = sizeOfBreadcrumbPath;
        panel.add(link);
        panel.updateUI();
        link.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
             for(int i = sizeOfBreadcrumbPath-1; i>=removeInt; i--){
                JLabel br =  BreadcrumbList.get(i);
                panel.remove(br);
                panel.updateUI();
                BreadcrumbList.remove(i);
                sizeOfBreadcrumbPath--;
             }
             for(int i=sizeOfBreadcrumb-1; i>= here ; i--){
                listOfNodes.remove(i);
                sizeOfBreadcrumb--;
            }
             removeContent();
             addIcons((ArrayList<File>) listOfNodes.get(listOfNodes.size()-1).listOfFiles);
             content.updateUI();
            }

        });
    }
    ////////// Add Icons //////////
    public void addIcons(java.util.ArrayList<File> listOfFiles){
         String imageName;
         String fileName;
         char check;
         int indexOf=0;
        JLabel periexomeno;
       enableMenu('u');

         Collections.sort(listOfFiles);
         for(int i =0; i<listOfFiles.size(); i++){
             indexOf = 0;
             fileName = listOfFiles.get(i).getName();
             for(int j =0; j<fileName.length(); j++){
                check = fileName.charAt(j);
                if(check == '.'){
                    indexOf = j;
                }
            }
             File kindOfFile = new File(listOfFiles.get(i).getAbsolutePath());
           if(!kindOfFile.isHidden()){
             //popupMenu
            JPopupMenu popupmenu = new JPopupMenu("Edit");
            JMenuItem cutpop = new JMenuItem("Cut");
            JMenuItem copypop = new JMenuItem("Copy");
            JMenuItem pastepop = new JMenuItem("Paste");
            JMenuItem renamepop = new JMenuItem("Rename");
            JMenuItem deletepop = new JMenuItem("Delete");
            JMenuItem favoritespop = new JMenuItem("Add to Favourites");
            JMenuItem propertiespop = new JMenuItem("Properties");
            pastepop.setEnabled(false);
            favoritespop.setEnabled(false);
            popupmenu.add(cutpop);
            popupmenu.add(copypop);
            popupmenu.add(pastepop);
            popupmenu.add(renamepop);
            popupmenu.add(deletepop);
            popupmenu.add(favoritespop);
            popupmenu.add(propertiespop);

            cutpop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(copySignal ==1 ){
                  copySignal =0;
                  copyFile = null;
                }
              cutSignal = 1;
              cutFile = new File(kindOfFile.getAbsolutePath());
            }
            });
            copypop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(cutSignal ==1 ){
                  cutSignal =0;
                  cutFile = null;
                }
              copySignal = 1;
              copyFile = new File(kindOfFile.getAbsolutePath());

            }
            });
            pastepop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            if(cutSignal == 1)
              cutpaste(cutFile,new File(kindOfFile.getAbsolutePath()+root.characteristic+cutFile.getName()));
             else copyPasteFunction(copyFile,new File(kindOfFile.getAbsolutePath()+root.characteristic+copyFile.getName()));
            }
            });
            renamepop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

              renameFunction(kindOfFile,listOfFiles);
            }
            });
            deletepop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              deleteFunction(kindOfFile,listOfFiles);
            }
            });
            propertiespop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               propertiesFunction(kindOfFile);
            }
            });
            favoritespop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean check;
               check = addToFavourites(kindOfFile.getName(),kindOfFile.getAbsolutePath());
               if(check == false){
                    JOptionPane.showMessageDialog(frame, "This directory is already added at favourites", "Inane error",JOptionPane.INFORMATION_MESSAGE);

               }
            }
            });
            if(kindOfFile.isDirectory()){
                ImageIcon diricon = new ImageIcon("./icons/folder.png");
                periexomeno = new JLabel();
                periexomeno.setPreferredSize(new Dimension(110,110));
                periexomeno.setIcon(diricon);
                periexomeno.setText(fileName);
                periexomeno.setVerticalAlignment(SwingConstants.CENTER);
                periexomeno.setHorizontalAlignment(SwingConstants.CENTER);
                periexomeno.setHorizontalTextPosition(SwingConstants.CENTER);
                periexomeno.setVerticalTextPosition(SwingConstants.BOTTOM);
                directoryLabels.add(directories,periexomeno);
                directories++;
                Border plaisio = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
                Border unselect = BorderFactory.createLineBorder(Color.WHITE);
                breadcrumbNode curr = new breadcrumbNode(kindOfFile,listOfNodes.size(),root.characteristic);
                int thisDir =directories ;
                int here = sizeOfBreadcrumb;
                File thisforselect = kindOfFile;
                JLabel thisperiexomeno = periexomeno;
                periexomeno.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1){
                        if(selected == 1){
                            selected = 0;
                            isSelected.setBorder(unselect);
                        }
                        JLabel ic =  directoryLabels.get(thisDir-1);
                        ic.setBorder(plaisio);
                        selectedInfo(ic,listOfNodes.get(listOfNodes.size()-1),thisforselect, 1);
                        enableMenu('e');
                        if(copySignal == 1 || cutSignal == 1){
                            enableMenu('p');
                        }
                         enableMenu('f');
                    }
                    if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1){
                        if(copySignal == 1)    pastepop.setEnabled(true);
                        if(cutSignal == 1)    pastepop.setEnabled(true);
                        favoritespop.setEnabled(true);
                        popupmenu.show(thisperiexomeno , e.getX(), e.getY());
                    }
                    if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2){
                        if( !kindOfFile.canRead()){
                            JOptionPane.showMessageDialog(frame, "You don't have access to this directory ", "Inane error",JOptionPane.ERROR_MESSAGE);

                        }
                        else{
                        listOfNodes.add(here,curr);
                        sizeOfBreadcrumb++;
                        addLink(curr.name,breadcrumbPanel,here+1);
                        removeContent();
                        addIcons((ArrayList<File>) listOfNodes.get(listOfNodes.size()-1).listOfFiles);
                        content.updateUI();
                        }
                   }
                }

                });

            }
            else{
                if(indexOf == fileName.length() ){
                    imageName =  "./icons/"+fileName;
                }
                else{
                    if( indexOf == 0){
                        imageName =  "./icons/"+fileName;
                    }else{
                        imageName =  "./icons/"+fileName.substring(indexOf+1);
                    }
                }
                imageName = imageName +".png";
                File image = new File(imageName);
                periexomeno = new JLabel();
                if(image.exists()){
                    ImageIcon fileicon = new ImageIcon(imageName);
                    periexomeno.setIcon(fileicon);

                }
                else{
                    ImageIcon queicon = new ImageIcon("./icons/question.png");
                    periexomeno.setIcon(queicon);

                }

                periexomeno.setPreferredSize(new Dimension(110,110));
                periexomeno.setText(fileName);
                periexomeno.setVerticalAlignment(SwingConstants.CENTER);
                periexomeno.setHorizontalAlignment(SwingConstants.CENTER);
                periexomeno.setHorizontalTextPosition(SwingConstants.CENTER);
                periexomeno.setVerticalTextPosition(SwingConstants.BOTTOM);
                fileLabels.add(files,periexomeno);
                files++;
                Border plaisio = BorderFactory.createLineBorder(Color.LIGHT_GRAY);
                Border unselect = BorderFactory.createLineBorder(Color.WHITE);
                int thisFile =files ;
                JLabel thisperiexomeno = periexomeno;
                File thisselected= kindOfFile;
                periexomeno.addMouseListener(new MouseAdapter() {

                     @Override
                     public void mouseClicked(MouseEvent e) {
                        if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1){

                            if(selected == 1){
                                selected = 0;
                                isSelected.setBorder(unselect);
                            }
                            JLabel ic =  fileLabels.get(thisFile-1);
                            ic.setBorder(plaisio);
                            selectedInfo(ic,listOfNodes.get(listOfNodes.size()-1),thisselected, 1);
                            enableMenu('e');
                            favourites.setEnabled(false);
                            paste.setEnabled(false);

                      }
                        if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1){
                                popupmenu.show(thisperiexomeno , e.getX(), e.getY());

                        }
                        if(SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2){
                            if(!kindOfFile.canRead()){
                                 JOptionPane.showMessageDialog(frame, "You don't have access to this file ", "Inane error",JOptionPane.ERROR_MESSAGE);

                            }
                            else{
                                    if(kindOfFile.canExecute()){
                                        try {
                                            Runtime.getRuntime().exec(kindOfFile.getAbsolutePath(), null, kindOfFile.getParentFile());
                                        } catch (IOException ex) {
                                            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    else{
                                         if(!Desktop.isDesktopSupported()){
                                           System.out.println("Desktop is not supported");
                                           return;
                                        }

                                        Desktop desktop = Desktop.getDesktop();
                                        if(kindOfFile.exists()) try {
                                            desktop.open(kindOfFile);
                                        } catch (IOException ex) {
                                            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                            }

                      }
                     }

                 });

            }
           }
         }

         for(int d=0; d< directories; d++){
         content.add(directoryLabels.get(d));

         }

         for(int f=0; f< files; f++){
         content.add(fileLabels.get(f));

         }
    }
    ////////// Remove Content //////////
    public void removeContent(){
       content.removeAll();
       directoryLabels.removeAll(directoryLabels);
       fileLabels.removeAll(fileLabels);
       directories =0;
       files = 0;
       selectedInfo(null,null,null,0);
       paste.setEnabled(false);
       enableMenu('u');
       content.updateUI();

    }
    ////////// Enable //////////
    public void enableMenu(char signal) {
        if(signal == 'e'){
            if(!cut.isEnabled()) cut.setEnabled(true);
            if(!copy.isEnabled()) copy.setEnabled(true);
            if(!rename.isEnabled()) rename.setEnabled(true);
            if(!favourites.isEnabled()) favourites.setEnabled(true);
            if(!delete.isEnabled()) delete.setEnabled(true);
        }
        if(signal == 'u'){
            if(cut.isEnabled()) cut.setEnabled(false);
            if(copy.isEnabled()) copy.setEnabled(false);
            if(rename.isEnabled()) rename.setEnabled(false);
            if(favourites.isEnabled()) favourites.setEnabled(false);
            if(delete.isEnabled()) delete.setEnabled(false);
        }
        if(signal == 'f'){
            if(!favourites.isEnabled()) favourites.setEnabled(true);
        }
        if(!paste.isEnabled() && signal== 'p')
           paste.setEnabled(true);
    }
    ////////// Selecte Info //////////
    public void selectedInfo(JLabel label,breadcrumbNode node,File file,int on){
        isSelected = label;
        isSelectedNode =node;
        isSelectedFile = file;
        selected = on;

     }
   ////////// Rename //////////
    public void renameFunction(File kindOfFile,java.util.ArrayList<File> listOfFiles){

         String renamed = JOptionPane.showInputDialog(frame, "RENAME",kindOfFile.getName());
        if(renamed == null ){
            //do nothing
        }
        else{
            int index =0;
            for(int j = 0; j< kindOfFile.getAbsolutePath().length(); j++){
                if(kindOfFile.getAbsolutePath().charAt(j) == root.characteristic){
                    index =j;
                }
            }
            String newname =kindOfFile.getAbsolutePath().substring(0, index+1);
            newname = newname +renamed;
            File newfile = new File(newname);
            if(newfile.exists()){
                JOptionPane.showMessageDialog(frame, "This name already exist at this file", "Inane error",JOptionPane.ERROR_MESSAGE);
            }
            else{
                changeFavourites(kindOfFile.getName() ,kindOfFile.getAbsolutePath(),newfile.getName(),newfile.getAbsolutePath());
                kindOfFile.renameTo(newfile);

                File parentdir = new File(listOfNodes.get(listOfNodes.size()-1).fullPath);
                if(parentdir.canRead()){
                    File []array;
                    array = new File[(int)parentdir.length()];
                    array = parentdir.listFiles();

                    for(int l =0; l<array.length; l++){
                        listOfFiles.remove(l);
                        listOfFiles.add(l,array[l]);
                    }
                }

                removeContent();
                addIcons((ArrayList<File>) listOfFiles);
                content.updateUI();
            }
        }
    }
   ////////// Delete //////////
   public void deleteFunction(File kindOfFile,java.util.ArrayList<File> listOfFiles){

        int permission = JOptionPane.showConfirmDialog(frame, "Are you sure?\nDo you want delete "+kindOfFile.getName(),"Inane warning",JOptionPane.WARNING_MESSAGE);
               if(permission == 0){
                   if(kindOfFile.isDirectory()){

                   deleteDirectory(kindOfFile);


                   }
                   else{
                     kindOfFile.delete();
                   }

                   File parentdir = new File(listOfNodes.get(listOfNodes.size()-1).fullPath);
                        if(parentdir.canRead()){

                         listOfFiles =new ArrayList<>();
                        File []array;
                        array = new File[(int)parentdir.length()];
                        array = parentdir.listFiles();


                        for(int l=0; l<array.length; l++){
                        listOfFiles.add(l,array[l]);


                        }
                        }

                       removeContent();
                       addIcons((ArrayList<File>) listOfFiles);
                       content.updateUI();
               }

   }
    ////////// Delete Recursion //////////
    public boolean deleteDirectory(File directoryToBeDeleted) {
        deleteFromFavourites(directoryToBeDeleted.getName() ,directoryToBeDeleted.getAbsolutePath());
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
   ////////// Properties //////////
    public void propertiesFunction(File kindOfFile){
       JCheckBox canRead = new JCheckBox("canRead");
       JCheckBox canWrite = new JCheckBox("canWrite");
       JCheckBox canExecute = new JCheckBox("canExecute");
       if(kindOfFile.canRead()){
           if(!kindOfFile.setReadable(false)){
               canRead.setEnabled(false);
           }
           else{
               kindOfFile.setReadable(true);
           }
           canRead.setSelected(true);

       }
       else{
            if(!kindOfFile.setReadable(true)){
               canRead.setEnabled(false);
           }
           else{
               kindOfFile.setReadable(false);
           }
           canRead.setSelected(false);

       }
       if(kindOfFile.canWrite()){
           if(!kindOfFile.setWritable(false)){
               canWrite.setEnabled(false);
           }
           else{
               kindOfFile.setWritable(true);
           }
           canWrite.setSelected(true);

       }
       else{
            if(!kindOfFile.setWritable(true)){
               canWrite.setEnabled(false);
           }
           else{
               kindOfFile.setWritable(false);
           }
           canWrite.setSelected(false);

       }
       if(kindOfFile.canExecute()){
           if(!kindOfFile.setExecutable(false)){
               canExecute.setEnabled(false);
           }
           else{
               kindOfFile.setExecutable(true);
           }
           canExecute.setSelected(true);

       }
       else{
            if(!kindOfFile.setExecutable(true)){
               canExecute.setEnabled(false);
           }
           else{
               kindOfFile.setExecutable(false);
           }
           canExecute.setSelected(false);

       }

       canRead.addItemListener(new ItemListener() {
             public void itemStateChanged(ItemEvent e) {
              if( e.getStateChange()==1){
                 kindOfFile.setReadable(true);
               }
               else{
                      kindOfFile.setReadable(false);
                }
             }
          });
      canExecute.addItemListener(new ItemListener() {
             public void itemStateChanged(ItemEvent e) {
              if( e.getStateChange()==1){
                 kindOfFile.setExecutable(true);
               }
               else{
                      kindOfFile.setExecutable(false);
                }
             }
          });
       canWrite.addItemListener(new ItemListener() {
             public void itemStateChanged(ItemEvent e) {
              if( e.getStateChange()==1){
                 kindOfFile.setWritable(true);
               }
               else{
                      kindOfFile.setWritable(false);
                }
             }
          });
       Object[] options = { canRead,canWrite,canExecute,"close"};
       String fileName = "NAME :            "+kindOfFile.getName()+"\n";
       String fullpath = "FULLPATH :    "+kindOfFile.getAbsolutePath()+"\n";
       String filesize = "SIZE :              "+totalsize(kindOfFile)+" (bytes)"+"\n";
       JOptionPane.showOptionDialog(null, fileName+fullpath+filesize+"PERMISSIONS :\n","Properties",JOptionPane.CLOSED_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[3]);

   }
   ////////// Total Size of File or Directory //////////
   public long totalsize(File directoryToBeSized){
        File[] allContents = directoryToBeSized.listFiles();
        long size = 0;
        if (allContents != null) {
            for (File file : allContents) {
                size = size + totalsize(file);
            }
        }
        size = size +directoryToBeSized.length();
        return size;
    }
   ////////// Copy //////////
    public void copyPasteFunction(File source , File destination){
       int permission =0;
       if(source.isDirectory()){
           copyFolder(source,destination);
       }
       else{
           try {
            if(destination.exists()){
                permission = JOptionPane.showConfirmDialog(frame, "Overide "+destination.getName()+" ?","Inane warning",JOptionPane.WARNING_MESSAGE);
                if(permission == 0){
                    Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
            else{
               Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
           } catch (IOException ex) {
               Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
           }

       }
       if(permission ==0){
            File parentdir = new File(listOfNodes.get(sizeOfBreadcrumb-1).fullPath);
            if(parentdir.canRead()){
                listOfNodes.get(sizeOfBreadcrumb-1).listOfFiles =new ArrayList<>();
                File []array;
                array = new File[(int)parentdir.length()];
                array = parentdir.listFiles();


                for(int l=0; l<array.length; l++){
                listOfNodes.get(sizeOfBreadcrumb-1).listOfFiles.add(l,array[l]);
                }
            }
            removeContent();
            addIcons(listOfNodes.get(sizeOfBreadcrumb-1).listOfFiles);
            content.updateUI();

       }
      paste.setEnabled(false);
      copyFile = null;
      copySignal =0;
   }


    ////////// Copy Folder //////////
    public void copyFolder(File source, File destination){
      int permission =0;
        if (source.isDirectory())
        {
            if (!destination.exists())
            {
                destination.mkdir();
            }
            else{
                permission = JOptionPane.showConfirmDialog(frame, "Overide "+destination.getName()+" ?","Inane warning",JOptionPane.WARNING_MESSAGE);
                if(permission !=0){
                    stopcopypaste =1;
                }
            }
            if(stopcopypaste != 1){
                String files[] = source.list();

                for (String file : files)
                {
                    if(stopcopypaste != 1){
                    File srcFile = new File(source, file);
                    File destFile = new File(destination, file);
                    //Recursive function call
                    copyFolder(srcFile, destFile);
                    }
                }
            }
        }
        else
        {

                if (destination.exists()){
                    permission = JOptionPane.showConfirmDialog(frame, "Overide "+destination.getName()+" ?","Inane warning",JOptionPane.WARNING_MESSAGE);
                    if(permission !=0){
                        stopcopypaste =1;
                    }
                }
            if(stopcopypaste != 1){
                try {
                    Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    ////////// Cut  //////////
    public void cutpaste(File source,File destination){

       if(destination.exists()){
            JOptionPane.showMessageDialog(frame, "Already exists a file/directory at this destination with the name: "+destination.getName(), "Inane error",JOptionPane.ERROR_MESSAGE);
        }
        else{
            changeFavourites(source.getName(),source.getAbsolutePath(),destination.getName(),destination.getAbsolutePath());
            source.renameTo(destination);
            File parentdir = new File(listOfNodes.get(sizeOfBreadcrumb-1).fullPath);
            if(parentdir.canRead()){
                listOfNodes.get(sizeOfBreadcrumb-1).listOfFiles =new ArrayList<>();
                File []array;
                array = new File[(int)parentdir.length()];
                array = parentdir.listFiles();


                for(int l=0; l<array.length; l++){
                listOfNodes.get(sizeOfBreadcrumb-1).listOfFiles.add(l,array[l]);
                }
            }
            removeContent();
            addIcons(listOfNodes.get(sizeOfBreadcrumb-1).listOfFiles);
            content.updateUI();

       }
        paste.setEnabled(false);
        cutFile = null;
        cutSignal =0;
     }
    ////////// search Function //////////
    public void searchFunction(String name,String type,File directoryToBeSearch){
        if(directoryToBeSearch.canRead()){
            File[] allContents = directoryToBeSearch.listFiles();

            if (allContents != null) {
                for (File file : allContents) {
                    searchFunction(name,type,new File(file.getAbsolutePath()));
                }
            }
            String newname;
            if(type == null){
                newname =directoryToBeSearch.getName().toLowerCase();
                if(newname.contains(name)){
                    listModel.addElement(directoryToBeSearch.getAbsolutePath());
                }
            }
            else{
                if("dir".equals(type)){
                    if(directoryToBeSearch.isDirectory()){
                        newname =directoryToBeSearch.getName().toLowerCase();
                        if(newname.contains(name)){
                            listModel.addElement(directoryToBeSearch.getAbsolutePath());
                        }
                    }
                }
                else{
                    newname =directoryToBeSearch.getName().toLowerCase();
                    if(newname.contains(name) && newname.endsWith(type)){
                        listModel.addElement(directoryToBeSearch.getAbsolutePath());

                    }
                }

            }
        }
    }
    ////////// Set Breadcrumb //////////
    public void setNewBreadcrumb(String path){
        String fileSeparator = "file.separator";
        String separator = System.getProperty(fileSeparator);
        char[] pathArray = path.toCharArray();
        String linkname = "";
        File directory ;
        breadcrumbNode curr;
        if(sizeOfBreadcrumb != 0){
            sizeOfBreadcrumb =0;
            for(int i =listOfNodes.size()-1; i>=0; i--){
                listOfNodes.remove(i);
            }
        }
        if(sizeOfBreadcrumbPath != 0){
            sizeOfBreadcrumbPath=0;
              for(int i =BreadcrumbList.size()-1; i>=0; i--){
                BreadcrumbList.remove(i);
            }
              breadcrumbPanel.removeAll();
              breadcrumbPanel.updateUI();
        }
        for(int i = 0; i< pathArray.length; i++){
            if(pathArray[i] == separator.charAt(0)){
                if("".equals(linkname) ){
                    linkname = linkname+pathArray[i];
                    directory = new File(linkname);
                    root = new breadcrumbNode(directory,listOfNodes.size(),separator.charAt(0));
                    listOfNodes.add(sizeOfBreadcrumb,root);
                    sizeOfBreadcrumb++;
                    addLink(root.name,breadcrumbPanel,sizeOfBreadcrumb);
                }
                else{

                    directory = new File(linkname);
                    if(root == null){
                        root = new breadcrumbNode(directory,listOfNodes.size(),separator.charAt(0));
                        listOfNodes.add(sizeOfBreadcrumb,root);
                        sizeOfBreadcrumb++;
                        addLink(root.name,breadcrumbPanel,sizeOfBreadcrumb);
                        linkname = linkname + pathArray[i];

                    }
                    curr = new breadcrumbNode(directory,listOfNodes.size(),root.characteristic);
                    listOfNodes.add(sizeOfBreadcrumb,curr);
                    sizeOfBreadcrumb++;
                    addLink(curr.name,breadcrumbPanel,sizeOfBreadcrumb);
                    linkname = linkname + pathArray[i];
                }
            }
            else{
            linkname = linkname + pathArray[i];
            if(i+1 == pathArray.length){
                directory = new File(linkname);

                curr = new breadcrumbNode(directory,listOfNodes.size(),root.characteristic);
                listOfNodes.add(sizeOfBreadcrumb,curr);
                sizeOfBreadcrumb++;
                addLink(curr.name,breadcrumbPanel,sizeOfBreadcrumb);
            }

            }

        }
    }
    ////////// Set Favourites //////////
    public void setFavourites(){

        String fileSeparator = "file.separator";
        String separator = System.getProperty(fileSeparator);
        if(favouritesSize > 0){
            for(int i=FavouritesList.size()-1; i>=0; i--){
                 FavouritesList.remove(i);
            }
            favouritesPanel.removeAll();
            favouritesSize =0;
        }
        else{
        favouritesPanel.removeAll();
        }

        String userHome = "user.home";
        String filepath = System.getProperty(userHome);
        filepath = filepath +separator+".java-file-browser"+separator+"properties.xml";
        Document doc = null;
        DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder Builder = null;
        try {
            Builder = Factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            doc = Builder.parse(new File(filepath));
        } catch (SAXException ex) {
            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
        }

        NodeList dirlist = doc.getElementsByTagName("Directory");
        Node dire;
        for(int k=dirlist.getLength()-1; k>=0; k--){
            dire= dirlist.item(k);

            Node name = dire.getAttributes().item(0);
            Node path = dire.getAttributes().item(1);
            String delname=name.getNodeValue();
            String delpath=path.getNodeValue();
            JPopupMenu popupdelete = new JPopupMenu();
            JMenuItem deletepop = new JMenuItem("Delete");
            deletepop.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    deleteFromFavourites(delname , delpath);
                }
            });
            popupdelete.add(deletepop);
            String nameOfLink = name.getNodeValue();
            JLabel link = new JLabel(nameOfLink);
            JLabel thislink = link;
            link.addMouseListener(new MouseAdapter() {

                     @Override
                     public void mouseClicked(MouseEvent e) {
                        if(SwingUtilities.isLeftMouseButton(e) ){
                            setNewBreadcrumb(path.getNodeValue());
                            removeContent();
                            addIcons((ArrayList<File>) listOfNodes.get(listOfNodes.size()-1).listOfFiles);
                      }
                        if(SwingUtilities.isRightMouseButton(e) ){
                                popupdelete.show(thislink, e.getX(), e.getY());

                        }
                }
            });
            link.setForeground(Color.BLUE.darker());
            link.setCursor(new Cursor(Cursor.HAND_CURSOR));
            FavouritesList.add(favouritesSize,link);
            favouritesSize++;
        }
        favtext =new JLabel("Favourites directories\n");
        favouritesPanel.add(favtext);
        for(int i=FavouritesList.size()-1; i>=0; i--){
            JLabel favlink = FavouritesList.get(i);
            favouritesPanel.add(favlink);
        }
        favouritesPanel.updateUI();

    }
    ////////// Add to Favourites //////////
    public boolean addToFavourites(String name ,String path){
        String fileSeparator = "file.separator";
        String separator = System.getProperty(fileSeparator);
                int allow =1;
                String userHome = "user.home";
                String dirpath = System.getProperty(userHome);
                dirpath = dirpath +separator+".java-file-browser";
                File dir = new File(dirpath);
                if(!dir.exists()){
                    dir.mkdir();
                }
                String namefile ="properties.xml";
                File FavFile = new File(dirpath+separator+namefile);
                Document doc = null;
                Element rootElement = null ;
                if(!FavFile.exists()){
                    try {
                        FavFile.createNewFile();
                        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                        doc = docBuilder.newDocument();
                       rootElement = doc.createElement("Favorites");
                        doc.appendChild(rootElement);
                    } catch (IOException ex) {
                        Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ParserConfigurationException ex) {
                        Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else{
                    DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder Builder = null;
                    try {
                        Builder = Factory.newDocumentBuilder();
                    } catch (ParserConfigurationException ex) {
                        Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    try {
                        doc = Builder.parse(new File(dirpath+separator+namefile));
                    } catch (SAXException ex) {
                        Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    rootElement = (Element) doc.getElementsByTagName("Favorites").item(0);
                }
                NodeList dirlist = doc.getElementsByTagName("Directory");
                Node dire;

                for(int k=0; k<dirlist.getLength(); k++){
                     dire= dirlist.item(k);
                     Node checkname = dire.getAttributes().item(0);
                     Node checkpath = dire.getAttributes().item(1);
                     if(checkname.getNodeValue().equals(name) && checkpath.getNodeValue().equals(path)){
                         allow =0;
                     }
                }
                if(allow == 1){
                    Element directory = doc.createElement("Directory");
                    directory.setAttribute("name", name);
                    directory.setAttribute("path", path);
                    rootElement.appendChild(directory);
                    Transformer transformer = null;
                    try {
                        transformer = TransformerFactory.newInstance().newTransformer();
                    } catch (TransformerConfigurationException ex) {
                        Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    try {
                        transformer.transform(new DOMSource(doc), new StreamResult(new File(dirpath+separator+namefile)));
                    } catch (TransformerException ex) {
                        Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    setFavourites();
                    return true;
                }
                else{
                    return false;

                }
    }
    ////////// Delete From Favourites//////////
    public void deleteFromFavourites(String name ,String path){
        String fileSeparator = "file.separator";
        String separator = System.getProperty(fileSeparator);
        String userHome = "user.home";
        String filepath = System.getProperty(userHome);
        filepath = filepath +separator+".java-file-browser"+separator+"properties.xml";
        Document doc = null;

        DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder Builder = null;
        try {
            Builder = Factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            doc = Builder.parse(new File(filepath));
        } catch (SAXException ex) {
            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        Element rootElement = (Element) doc.getElementsByTagName("Favorites").item(0);
        NodeList dirlist = doc.getElementsByTagName("Directory");
        Node dire;
        Element deleteElement = null;
        Node deleteNode = null;
          for(int k=0; k<dirlist.getLength(); k++){
               dire= dirlist.item(k);
               Node checkname = dire.getAttributes().item(0);
               Node checkpath = dire.getAttributes().item(1);

               if(checkname.getNodeValue().equals(name) && checkpath.getNodeValue().equals(path)){

                   deleteElement = (Element) dirlist.item(k);
                   deleteNode=deleteElement.getPreviousSibling();
               }
        }
          if(deleteElement != null){
            rootElement.removeChild(deleteElement);
            rootElement.removeChild(deleteNode);
            doc.normalize();
            Transformer transformer = null;
                try {
                    transformer = TransformerFactory.newInstance().newTransformer();
                } catch (TransformerConfigurationException ex) {
                    Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                }
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                try {
                    transformer.transform(new DOMSource(doc), new StreamResult(new File(filepath)));
                } catch (TransformerException ex) {
                    Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
            }
                    setFavourites();
          }
          else{
              setFavourites();
          }

    }
      ////////// change Favourites//////////
    public void changeFavourites(String name ,String path,String newName,String newPath){
        String fileSeparator = "file.separator";
        String separator = System.getProperty(fileSeparator);
        String userHome = "user.home";
        String filepath = System.getProperty(userHome);
        filepath = filepath +separator+".java-file-browser"+separator+"properties.xml";
        Document doc = null;

        DocumentBuilderFactory Factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder Builder = null;
        try {
            Builder = Factory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            doc = Builder.parse(new File(filepath));
        } catch (SAXException ex) {
            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
        }
        NodeList dirlist = doc.getElementsByTagName("Directory");
        Node dire;
        int change=0;
          for(int k=0; k<dirlist.getLength(); k++){
               dire= dirlist.item(k);
               Node checkname = dire.getAttributes().item(0);
               Node checkpath = dire.getAttributes().item(1);

               if(checkname.getNodeValue().equals(name) && checkpath.getNodeValue().equals(path)){
                    checkname.setNodeValue(newName);
                    checkpath.setNodeValue(newPath);
                    change = 1;
               }
               else{
                   if(checkpath.getNodeValue().contains(path)){
                       String pathforchange = checkpath.getNodeValue().replace(path, newPath);
                       checkpath.setNodeValue(pathforchange);
                       change = 1;
                   }
               }

        }
          if(change == 1){

            doc.normalize();
            Transformer transformer = null;
                try {
                    transformer = TransformerFactory.newInstance().newTransformer();
                } catch (TransformerConfigurationException ex) {
                    Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
                }
                transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");

                try {
                    transformer.transform(new DOMSource(doc), new StreamResult(new File(filepath)));
                } catch (TransformerException ex) {
                    Logger.getLogger(Ce326Hw3.class.getName()).log(Level.SEVERE, null, ex);
            }
                    setFavourites();
          }
          else{
              setFavourites();
          }

    }
}
