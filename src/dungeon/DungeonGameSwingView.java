package dungeon;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

/**
 * This is an implementation of the Dungeon Game View interface that uses Java Swing to
 * draw the game state. It shows the current game state and a birds eye view of the dungeon
 * as it is being explored by the player.
 */
public class DungeonGameSwingView extends JFrame implements DungeonGameView {
  JMenuBar menuBar;
  JMenu menu;
  JMenuItem newGame;
  JMenuItem restartGame;
  JMenuItem exitGame;
  JDialog jd;
  private JPanel infoPanel;
  private JPanel dungeonPanel;
  JScrollPane panelPane;
  JSplitPane splitPane;
  JButton modelInputs;
  JSpinner row;
  JSpinner col;
  JSpinner deg;
  JSpinner perc;
  JSpinner difficulty;
  JComboBox wrap;
  GameModelReadOnly model;
  private boolean isShiftPressed;
  private boolean isWrap;
  private String infoPanelMsg = "";

  /**
   * Constructor for the Dungeon Game view that initializes the panel and frame parameters for the
   * game to be played on.
   */
  public DungeonGameSwingView() {
    super("Dungeon Game");
    setLayout(new BorderLayout());
    this.setSize(500, 500);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setUndecorated(true);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    addMenu();
    isShiftPressed = false;
    isWrap = false;
    modelInputs = new JButton("Ok");
    infoPanel = new JPanel();
    infoPanel.setBackground(Color.LIGHT_GRAY);
    infoPanel.setVisible(true);
    dungeonPanel = new JPanel();
    dungeonPanel.setBackground(Color.gray);
    dungeonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    dungeonPanel.setVisible(true);
    panelPane = new JScrollPane(dungeonPanel);
    splitPane = new JSplitPane();
    splitPane.setSize(600, 750);
    splitPane.setDividerSize(3);
    splitPane.setDividerLocation(400);
    splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    splitPane.setLeftComponent(infoPanel);
    splitPane.setRightComponent(panelPane);
    this.add(splitPane);
    this.setVisible(true);
  }

  private void addMenu() {
    menuBar = new JMenuBar();
    menu = new JMenu("Options");

    JMenuItem instructions = new JMenuItem("Instructions");
    menu.add(instructions);
    instructions.addActionListener((ActionEvent e) -> {
      String instrMsg = "Starting New Game: Options -> New Game (Enter valid inputs to "
              + "generate the dungeon)\n\nRestart Previous Game: Options -> Restart Game\n\n"
              + "Quit Game Window: Options -> Exit Game\n\n"
              + "Please use the following Commands to play\n\n"
              + "Player Movement -> Directional arrow keys / Mouse click at any immediate "
              + "neighbouring location of the player\n\nPick Arrows -> Key 'A' on the keyboard\n\n"
              + "Pick Treasure -> Key 'T' on the keyboard\n\n"
              + "Shoot Arrows -> 1. Shift + Directional arrow keys, 2. Enter Distance in the "
              + "popup box";
      JOptionPane.showMessageDialog(null, instrMsg,
              "Instructions", JOptionPane.INFORMATION_MESSAGE);
    });

    newGame = new JMenuItem("New Game");
    menu.add(newGame);
    newGame.addActionListener(e -> createNewGame());

    restartGame = new JMenuItem("Restart Game");
    menu.add(restartGame);

    exitGame = new JMenuItem("Exit Game");
    menu.add(exitGame);
    exitGame.addActionListener((ActionEvent e) -> System.exit(0));

    menuBar.add(menu);
    this.add(menuBar, BorderLayout.NORTH);
  }

  private void createNewGame() {
    jd = new JDialog(this);
    jd.setBounds(500, 300, 350, 300);
    JPanel rowPanel;

    JPanel panel = new JPanel();
    BoxLayout boxlayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
    panel.setLayout(boxlayout);

    rowPanel = new JPanel();
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    JLabel label = new JLabel("Enter number of Rows");
    rowPanel.add(label);
    SpinnerNumberModel numberModel = new SpinnerNumberModel();
    row = new JSpinner(numberModel);
    row.setPreferredSize(new Dimension(50, 20));
    rowPanel.add(row);
    panel.add(rowPanel);

    rowPanel = new JPanel();
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    label = new JLabel("Enter number of Columns");
    rowPanel.add(label);
    numberModel = new SpinnerNumberModel();
    col = new JSpinner(numberModel);
    col.setPreferredSize(new Dimension(50, 20));
    rowPanel.add(col);
    panel.add(rowPanel);

    rowPanel = new JPanel();
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    label = new JLabel("Enter Degree of Interconnectivity");
    rowPanel.add(label);
    numberModel = new SpinnerNumberModel();
    deg = new JSpinner(numberModel);
    deg.setPreferredSize(new Dimension(50, 20));
    rowPanel.add(deg);
    panel.add(rowPanel);

    rowPanel = new JPanel();
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    label = new JLabel("Is Dungeon Wrappable? ");
    rowPanel.add(label);
    wrap = new JComboBox(new String[]{"Yes", "No"});
    wrap.setPreferredSize(new Dimension(80, 20));
    wrap.setSelectedIndex(1);
    rowPanel.add(wrap);
    panel.add(rowPanel);

    rowPanel = new JPanel();
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    label = new JLabel("Enter Percentage of Item assignment");
    rowPanel.add(label);
    numberModel = new SpinnerNumberModel();
    perc = new JSpinner(numberModel);
    perc.setPreferredSize(new Dimension(50, 20));
    rowPanel.add(perc);
    panel.add(rowPanel);

    rowPanel = new JPanel();
    rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    label = new JLabel("Enter Count of Otyughs");
    rowPanel.add(label);
    numberModel = new SpinnerNumberModel();
    difficulty = new JSpinner(numberModel);
    difficulty.setPreferredSize(new Dimension(50, 20));
    rowPanel.add(difficulty);
    panel.add(rowPanel);

    panel.add(modelInputs);

    jd.add(panel);
    jd.setVisible(true);
  }

  @Override
  public void addModelInputListener(GameControllerSwing listener) {
    modelInputs.addActionListener(e -> {
      int rowCount = (int) row.getValue();
      int colCount = (int) col.getValue();
      int degCount = (int) deg.getValue();
      int percentage = (int) perc.getValue();
      int otyughCount = (int) difficulty.getValue();
      isWrap = false;
      if (wrap.getSelectedIndex() == 0) {
        isWrap = true;
      }
      try {
        listener.createModel(rowCount, colCount, degCount, isWrap, percentage, otyughCount);
        jd.dispose();
      } catch (IllegalArgumentException a) {
        JOptionPane.showMessageDialog(null, "Please enter valid inputs",
                "Invalid Inputs", JOptionPane.ERROR_MESSAGE);
      }
    });
  }

  @Override
  public void addRestartGameListener(GameControllerSwing listener) {
    restartGame.addActionListener(e -> {
      listener.restartGame();
    });
  }

  @Override
  public void addKeyListener(GameControllerSwing dungeonGameSwingController) {
    this.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        //no key typed event used
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
          isShiftPressed = true;
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
          isShiftPressed = false;
        }
        if (!model.isGameOver()) {
          if (isShiftPressed) {
            isShiftPressed = false;
            dungeonGameSwingController.handleComboKeyEvent(e);
          } else {
            dungeonGameSwingController.handleSingleKeyEvent(e);
          }
        } else if (model.isGameOver() && !model.isPlayerDead()) {
          refresh("You completed the maze..GAME OVER");
        }
      }
    });
  }

  @Override
  public int addShootDistanceListener() {
    int distance = 0;
    String inputValue = JOptionPane.showInputDialog("Enter Shoot Distance");
    try {
      distance = Integer.parseInt(inputValue);
    } catch (NumberFormatException n) {
      JOptionPane.showMessageDialog(null,
              "Please enter valid numerical values",
              "Invalid Inputs", JOptionPane.ERROR_MESSAGE);
      distance = addShootDistanceListener();
    }
    return distance;
  }

  @Override
  public void setModel(GameModelReadOnly model) {
    this.model = model;
  }

  @Override
  public void makeVisible() {
    setVisible(true);
  }

  @Override
  public void addClickListener(GameControllerSwing listener) {
    // create the MouseAdapter
    MouseAdapter clickAdapter = new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        // arithmetic to convert panel coords to grid coords
        int x = e.getX();
        int y = e.getY();
        if (!model.isGameOver()) {
          listener.handleCellClick(x, y, isWrap, (int) row.getValue(), (int) col.getValue());
        } else if (model.isGameOver() && !model.isPlayerDead()) {
          refresh("You completed the maze..GAME OVER");
        }
      }
    };
    dungeonPanel.addMouseListener(clickAdapter);
  }

  @Override
  public void refresh(String message) {
    if (!message.equals("")) {
      infoPanelMsg = message;
    }
    infoPanel.removeAll();
    dungeonPanel.removeAll();
    JPanel rowPanel;
    BoxLayout boxlayout = new BoxLayout(infoPanel, BoxLayout.Y_AXIS);
    infoPanel.setLayout(boxlayout);

    try {
      //Player image
      rowPanel = new JPanel();
      rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      Image overlay = ImageIO.read(getClass().getResourceAsStream("/player.png"));
      overlay = overlay.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      Icon img = new ImageIcon(overlay);

      //player bag info
      JLabel playerInfo = new JLabel();
      playerInfo.setIcon(img);
      rowPanel.add(playerInfo);
      JTextField bagCount = new JTextField();
      bagCount.setText(model.getPlayerDescription());
      bagCount.setEditable(false);
      rowPanel.add(bagCount);
      infoPanel.add(rowPanel);

      //location image
      rowPanel = new JPanel();
      rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      String path;
      if (model.getLocationType(model.getPlayerLocation()).equals("Tunnel")) {
        path = "/NS.png";
      } else {
        path = "/N.png";
      }
      overlay = ImageIO.read(getClass().getResourceAsStream(path));
      overlay = overlay.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
      img = new ImageIcon(overlay);
      JLabel locationInfo = new JLabel();
      locationInfo.setIcon(img);
      rowPanel.add(locationInfo);

      //location info
      String items = model.getAvailableItems(model.getPlayerLocation());
      if (items.equals("")) {
        items = "No items at this location.";
      }
      JTextField locItemCount = new JTextField();
      locItemCount.setText(items);
      locItemCount.setEditable(false);
      rowPanel.add(locItemCount);
      infoPanel.add(rowPanel);

      //Current Game state
      rowPanel = new JPanel();
      rowPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      JTextField gameStatus = new JTextField();
      gameStatus.setText(infoPanelMsg);
      gameStatus.setEditable(false);
      rowPanel.add(gameStatus);
      infoPanel.add(rowPanel);

    } catch (IOException e) {
      e.printStackTrace();
    }

    int rowCount = (int) row.getValue();
    int colCount = (int) col.getValue();

    JPanel container = new JPanel();
    container.setPreferredSize(new Dimension(64 * colCount, 64 * rowCount));
    container.setVisible(true);

    GridBagLayout gridbag = new GridBagLayout();
    gridbag.preferredLayoutSize(container);
    GridBagConstraints c = new GridBagConstraints();
    container.setLayout(gridbag);

    for (int i = 0; i < rowCount; i++) {
      for (int j = 0; j < colCount; j++) {
        Icon img = getImage(i, j);
        if (img == null) {
          try {
            img = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/blank.png")));
          } catch (IOException e) {
            throw new IllegalStateException("Image not found");
          }
        }
        JLabel imgLabel = new JLabel();
        imgLabel.setIcon(img);
        c.gridx = j;
        c.gridy = i;
        container.add(imgLabel, c);
      }
    }
    dungeonPanel.add(container);
    infoPanel.updateUI();
    dungeonPanel.updateUI();
  }

  private Icon getImage(int row, int col) {
    try {
      String nextMoves = "";
      Icon img;
      BufferedImage imgFinal = null;
      String loc = "" + row + "-" + col;

      nextMoves = model.getNextPossibleMoves(loc)
              .replaceAll("\\s", "");

      if (nextMoves.equals("") || !model.getAllVisitedLocations().contains("" + row + "-" + col)) {
        return null;
      } else {
        String imgPath = "/" + nextMoves + ".png";

        try {
          img = new ImageIcon(ImageIO.read(getClass().getResourceAsStream(imgPath)));
          imgFinal = getBufferedImageFromIcon(img);

          //check for diamond in this location
          if (model.getDiamondCount(loc) > 0) {
            imgFinal = overlay(imgFinal, "/diamond.png", 20,
                    true, 8, 8);
          }
          if (model.getSapphireCount(loc) > 0) {
            imgFinal = overlay(imgFinal, "/sapphire.png", 25,
                    true, 8, 8);
          }
          if (model.getRubyCount(loc) > 0) {
            imgFinal = overlay(imgFinal, "/ruby.png", 30,
                    true, 8, 8);
          }
          //check for arrows in this location
          if (model.getArrowCount(loc) > 0) {
            imgFinal = overlay(imgFinal, "/arrow-white.png", 20,
                    true, 12, 6);
          }
          //check for smell in this location
          if (model.getLocationSmell(loc).equals("Bad") && !model.getOtyughLocations()
                  .contains(loc)) {
            imgFinal = overlay(imgFinal, "/stench01.png", 0,
                    false, 0, 0);
          }
          if (model.getLocationSmell(loc).equals("Terrible") && !model.getOtyughLocations()
                  .contains(loc)) {
            imgFinal = overlay(imgFinal, "/stench02.png", 0,
                    false, 0, 0);
          }
          //check for player in this location
          if (model.getPlayerLocation().equals(loc)) {
            imgFinal = overlay(imgFinal, "/player.png", 20,
                    true, 25, 25);
          }

          //check for monster in this location
          if (model.getOtyughLocations().contains(loc)) {
            imgFinal = overlay(imgFinal, "/otyugh.png", 10,
                    true, 30, 30);
          }
        } catch (IOException e) {
          throw new IllegalStateException("Image not found");
        }
        return new ImageIcon(imgFinal);
      }
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  //http://www.java2s.com/example/java-utility-method/icon-to-bufferedimage-index-0.html
  private BufferedImage getBufferedImageFromIcon(Icon icon) {
    BufferedImage buffer = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
            BufferedImage.TYPE_INT_ARGB);
    Graphics g = buffer.getGraphics();
    icon.paintIcon(new JLabel(), g, 0, 0);
    g.dispose();
    return buffer;
  }

  /**
   * Gets a BufferedImage, overlays a new image on it, and then returns the new combined image.
   *
   * @param starting The base image. This is the image that needs to have another image layered
   *                 over it.
   * @param fpath    The path of the image that should be superimposed i.e. the image on top of
   *                 the base image.
   * @param offset   The offset by which the top image should be superimposed on the base image.
   * @return          The combined image where the image in 'fpath' is superimposed on top of
   *                  the image in 'starting'
   * @throws IOException Thrown when fpath is not found?
   */
  private BufferedImage overlay(BufferedImage starting, String fpath, int offset,
                                boolean scaleDown, int newWidth, int newHeight) throws IOException {
    Image overlay = ImageIO.read(getClass().getResourceAsStream(fpath));
    if (scaleDown) {
      overlay = overlay.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
    }
    BufferedImage combined = new BufferedImage(starting.getWidth(), starting.getHeight(),
            BufferedImage.TYPE_INT_ARGB);
    Graphics g = combined.getGraphics();
    g.drawImage(starting, 0, 0, null);
    int yOffset = 20;
    if (fpath.contains("arrow")) {
      yOffset = 30;
    } else if (fpath.contains("stench")) {
      yOffset = 0;
    }
    g.drawImage(overlay, offset, yOffset, null);
    return combined;
  }
}
