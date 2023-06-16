package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.InputMismatchException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import manager.MakerManager;
import model.Stat;
import model.Unit;
import model.UnitClass;
import model.UnitType;
import model.Weapon;
import model.WeaponType;


public class FEMakerUI extends JFrame {
	/**Obligatory serial version*/
	private static final long serialVersionUID = 1L;

	private JPanel p;

	private CardLayout cl;
	
	public static final String VIEW = "View";
	
	public FEMakerUI() {

		p = new JPanel();
		cl = new CardLayout();
		p.setLayout(cl);
		
		//Window formatting
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(900, 700);
	    setTitle("Simple Fire Emblem Maker");

	    p.add(new IntroPanel(), "Intro");	    
	    cl.show(p, "Intro");
	    getContentPane().add(p, BorderLayout.CENTER);

	    setVisible(true);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		FEMakerUI hg = new FEMakerUI();
	}
	
	private void switchToPanel(JPanel panel, String name) {
		p.add(panel, name);
		cl.show(p, name);
		validate();
		repaint();
	}	
	private String validateInputName(String s, String field) {
		//Assume the string is not null or empty, because that just means a random name
		//should be used
		if (s == null || s.equals("")) {
			throw new IllegalArgumentException("Input for " + field + " cannot be null or empty");
		}
		s.trim();
//			if (s.length() > 24) {
//				throw new IllegalArgumentException("Too long name for " + field);
//			}
		if (s.length() < 1) {
			throw new IllegalArgumentException("Input for " + field + " cannot just be whitespace");
		}
			for (int q = 0; q < s.length(); q++) {
				char c = s.charAt(q);
				if (!Character.isLetter(c)
						&& !Character.isDigit(c)
						&& c != ' '
						&& c != '-'
						&& c != '_'
						&& c != '.'
						) {
					throw new IllegalArgumentException(field + " contains an illegal character");
				}
			}
		return s;
	}
	private int parseValidDigitWithinBounds(String s, int min, int max, String field)
			throws IllegalArgumentException {
		s.trim();
		try {
			int num = Integer.parseInt(s);
			if (num >= min && num <= max) {
				return num;
			}
		} catch (InputMismatchException e) {
			throw new IllegalArgumentException("Entered a non-integer for " + field);
		}
		throw new IllegalArgumentException("Entered an out-of-bounds value for " + field);
	}

	private class IntroPanel extends JPanel {
		public IntroPanel() {
			setLayout(new BorderLayout());
			
			JButton start = new JButton("SET STATS");
			start.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new DecideStatsPanel(), VIEW);
					JOptionPane.showMessageDialog(null, "<html>Decide what stats units should have<br/>"
							+ "<html>Like Strength (STR), Speed (SPD), etc.<br/>"
							+ "<html>Don't worry about HP, as that is a given<br/>");
				}
			});
			
			JButton useDefaults = new JButton("USE DEFAULTS");
			useDefaults.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MakerManager.setDefaults();
					switchToPanel(new CreateElementsPanel(), VIEW);
				}
			});
			
			JPanel options = new JPanel(new GridLayout(1, 2));
			options.add(start);
			options.add(useDefaults);
			
			add(new JLabel("Here will be a cool picture"));
			add(options, BorderLayout.PAGE_END);
		}
	}
	
	private class DecideStatsPanel extends JPanel {
		public DecideStatsPanel() {
			setLayout(new BorderLayout());
			
			JPanel currentStats = new JPanel(new GridLayout(MakerManager.stats.size(), 5));
//			currentStats.add(new JLabel("Stat"));
//			currentStats.add(new JLabel("Abbreviation"));
//			currentStats.add(new JLabel("Min Value"));
//			currentStats.add(new JLabel("Max Value"));
//			currentStats.add(new JLabel("blank"));
			for (int q = 0; q < MakerManager.stats.size(); q++) {
				Stat stat = MakerManager.stats.get(q);
				int idx = q;
				currentStats.add(new JLabel(stat.getName()));
				currentStats.add(new JLabel(stat.getAbbreviation()));
				currentStats.add(new JLabel("" + stat.getAbsoluteMin()));
				currentStats.add(new JLabel("" + stat.getAbsoluteMax()));
				JButton delete = new JButton("Delete");
				delete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						MakerManager.stats.remove(idx);
						switchToPanel(new DecideStatsPanel(), VIEW);
					}
				});
				currentStats.add(delete);
			}
//			int numLeft = 8 - MakerManager.stats.size();
//			for (int q = 0; q < numLeft; q++) {
//				currentStats.add(new JLabel());
//				currentStats.add(new JLabel());
//				currentStats.add(new JLabel());
//				currentStats.add(new JLabel());
//				currentStats.add(new JLabel());
//			}
			
			JTextField nameField = new JTextField();
			JTextField abbrField = new JTextField();
			JTextField minField = new JTextField();
			JTextField maxField = new JTextField();
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						String name = validateInputName(nameField.getText(), "Name");
						String abbr = validateInputName(abbrField.getText(), "Abbreviation");
						int min = parseValidDigitWithinBounds(minField.getText(), 0, Integer.MAX_VALUE, "Min Value");
						int max = parseValidDigitWithinBounds(maxField.getText(), min, Integer.MAX_VALUE, "Max Value");
						Stat stat = new Stat(name, abbr, min, max);
						MakerManager.stats.add(stat);
						switchToPanel(new DecideStatsPanel(), VIEW);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			
			JButton done = new JButton("<html>Done<br/><html>Next<br/>");
			done.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new WeaponStatsPanel(), VIEW);
					JOptionPane.showMessageDialog(null, "Now, pick the stats for weapons");
				}
			});
			
			JPanel bottom = new JPanel(new GridLayout(1, 10));
			bottom.add(new JLabel("Name"));
			bottom.add(nameField);
			bottom.add(new JLabel("Abbreviation"));
			bottom.add(abbrField);
			bottom.add(new JLabel("Min Value"));
			bottom.add(minField);
			bottom.add(new JLabel("Max Value"));
			bottom.add(maxField);
			bottom.add(confirm);
			bottom.add(done);
			
			add(new JLabel("Unit Stats"), BorderLayout.PAGE_START);
			add(new JScrollPane(currentStats));
			add(bottom, BorderLayout.PAGE_END);
		}
	}

	private class WeaponStatsPanel extends JPanel {
		public WeaponStatsPanel() {
			setLayout(new BorderLayout());
			
			JPanel currentStats = new JPanel(new GridLayout(MakerManager.wepStats.size(), 5));
//			currentStats.add(new JLabel("Stat"));
//			currentStats.add(new JLabel("Abbreviation"));
//			currentStats.add(new JLabel("Min Value"));
//			currentStats.add(new JLabel("Max Value"));
//			currentStats.add(new JLabel("blank"));
			for (int q = 0; q < MakerManager.wepStats.size(); q++) {
				Stat stat = MakerManager.wepStats.get(q);
				int idx = q;
				currentStats.add(new JLabel(stat.getName()));
				currentStats.add(new JLabel(stat.getAbbreviation()));
				currentStats.add(new JLabel("" + stat.getAbsoluteMin()));
				currentStats.add(new JLabel("" + stat.getAbsoluteMax()));
				JButton delete = new JButton("Delete");
				delete.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						MakerManager.wepStats.remove(idx);
						switchToPanel(new WeaponStatsPanel(), VIEW);
					}
				});
				currentStats.add(delete);
			}
//			int numLeft = 8 - MakerManager.stats.size();
//			for (int q = 0; q < numLeft; q++) {
//				currentStats.add(new JLabel());
//				currentStats.add(new JLabel());
//				currentStats.add(new JLabel());
//				currentStats.add(new JLabel());
//				currentStats.add(new JLabel());
//			}
			
			JTextField nameField = new JTextField();
			JTextField abbrField = new JTextField();
			JTextField minField = new JTextField();
			JTextField maxField = new JTextField();
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						String name = validateInputName(nameField.getText(), "Name");
						String abbr = validateInputName(abbrField.getText(), "Abbreviation");
						int min = parseValidDigitWithinBounds(minField.getText(), 0, Integer.MAX_VALUE, "Min Value");
						int max = parseValidDigitWithinBounds(maxField.getText(), min, Integer.MAX_VALUE, "Max Value");
						Stat stat = new Stat(name, abbr, min, max);
						MakerManager.wepStats.add(stat);
						switchToPanel(new WeaponStatsPanel(), VIEW);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			
			JButton done = new JButton("<html>Done<br/><html>Next<br/>");
			done.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new DecideCalculationsPanel(), VIEW);
					JOptionPane.showMessageDialog(null, "<html>Decide the calculation for Unit Accuracy<br/>"
							+ "<html><br/><html>Please double check it, as there is no error checking<br/>");

				}
			});
			
			JPanel bottom = new JPanel(new GridLayout(1, 10));
			bottom.add(new JLabel("Name"));
			bottom.add(nameField);
			bottom.add(new JLabel("Abbreviation"));
			bottom.add(abbrField);
			bottom.add(new JLabel("Min Value"));
			bottom.add(minField);
			bottom.add(new JLabel("Max Value"));
			bottom.add(maxField);
			bottom.add(confirm);
			bottom.add(done);
			
			add(new JLabel("Weapon Stats"), BorderLayout.PAGE_START);
			add(new JScrollPane(currentStats));
			add(bottom, BorderLayout.PAGE_END);
		}
	}
	
	private class DecideCalculationsPanel extends JPanel {
		public DecideCalculationsPanel() {
			setLayout(new BorderLayout());
			
			JTextField calc = new JTextField();
			calc.enableInputMethods(false);
			
			String[] calculations = {"Unit Accuracy", "Unit Avoidance", "Unit Critical Hit",
					"Unit Critical Avoid", "Unit Staff Accuracy", "Unit Staff Avoidance",
					"Unit Attack Speed", "Unit Physical Might", "Unit Physical Defense", 
					"Unit Magic Might", "Unit Magic Defense"};
//			String[] defaultCalculations = {"#2+#4+W1", "#3*2+#4", "#2+W2",
//					"#4", "#1*2+#4", "#6*2+#4",
//					"#3-P(W3-#0)", "#0+W0", "#5", 
//					"#1+W0", "#6"};
			ArrayList<String> usedCalculations = new ArrayList<>();

			JPanel numberPad = new JPanel(new GridLayout(5, 4));
			String[] additions = {"P(", "(", ")", "/", "7", "8", "9", "*", "4", "5",
					"6", "-", "1", "2", "3", "+", "", "0", ""};
			String[] titles = {"P(", "(", ")", "/", "7", "8", "9", "*", "4", "5",
					"6", "-", "1", "2", "3", "+", "", "0", ""};
			for (int q = 0; q < additions.length; q++) {
				JButton button = new JButton(titles[q]);
				int idx = q;
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						calc.setText(calc.getText() + additions[idx]);
					}
				});
				numberPad.add(button);
			}
			JButton delete = new JButton("<<");
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						calc.setText(calc.getText(0, calc.getText().length() - 1));
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			numberPad.add(delete);
			
			JPanel unitStats = new JPanel(new GridLayout(MakerManager.stats.size(), 1));
			for (int q = 0; q < MakerManager.stats.size(); q++) {
				Stat s = MakerManager.stats.get(q);
				int idx = q;
				JButton button = new JButton(String.format("%s (%s)", s.getName(), s.getAbbreviation()));
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						calc.setText(calc.getText() + "#" + idx);
					}
				});
				unitStats.add(button);
			}
			
			JPanel wepStats = new JPanel(new GridLayout(MakerManager.wepStats.size(), 1));
			for (int q = 0; q < MakerManager.wepStats.size(); q++) {
				Stat s = MakerManager.wepStats.get(q);
				int idx = q;
				JButton button = new JButton(String.format("%s (%s)", s.getName(), s.getAbbreviation()));
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						calc.setText(calc.getText() + "W" + idx);
					}
				});
				wepStats.add(button);
			}

//			JButton useDefault = new JButton("Use Default");
//			useDefault.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					calc.setText(defaultCalculations[usedCalculations.size()]);
//				}
//			});
			
			JLabel pageTitle = new JLabel(calculations[0]);
			
			JButton done = new JButton("Next");
			done.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					usedCalculations.add(calc.getText());
					calc.setText("");
					if (usedCalculations.size() < calculations.length) {
						String calcName = calculations[usedCalculations.size()];
						pageTitle.setText(calcName);
						pageTitle.validate();
						pageTitle.repaint();
						JOptionPane.showMessageDialog(null, "<html>Decide the calculation for " + calcName + "<br/>"
								+ "<html><br/><html>Please double check it, as there is no error checking<br/>");
					} else {
						MakerManager.accuracyCalculation = MakerManager.convertToRPN(usedCalculations.get(0)); //Skill times 2 plus luck plus weapon-accuracy
						MakerManager.avoidanceCalculation = MakerManager.convertToRPN(usedCalculations.get(1)); //Speed times 2 plus luck
						MakerManager.criticalCalculation = MakerManager.convertToRPN(usedCalculations.get(2)); //Skill plus weapon-critical
						MakerManager.critAvoidCalculation = MakerManager.convertToRPN(usedCalculations.get(3)); //Luck
						MakerManager.staffAccuracyCalculation = MakerManager.convertToRPN(usedCalculations.get(4)); //Magic times 2 plus luck
						MakerManager.staffAvoidCalculation = MakerManager.convertToRPN(usedCalculations.get(5)); //Resistance times 2 plus luck
						MakerManager.attackSpeedCalculation = MakerManager.convertToRPN(usedCalculations.get(6)); //Speed minus max between 0 and (weapon-weight minus strength)
						MakerManager.physicalMightCalculation = MakerManager.convertToRPN(usedCalculations.get(7)); //Strength plus weapon-might
						MakerManager.physicalDefenseCalculation = MakerManager.convertToRPN(usedCalculations.get(8)); //Defense
						MakerManager.magicMightCalculation = MakerManager.convertToRPN(usedCalculations.get(9)); //Magic plus weapon-might
						MakerManager.magicDefenseCalculation = MakerManager.convertToRPN(usedCalculations.get(10)); //Resistance
	
						switchToPanel(new CreateElementsPanel(), VIEW);
					}
				}
			});
			
			JPanel inputs = new JPanel(new GridLayout(1, 4));
			inputs.add(numberPad);
			inputs.add(new JScrollPane(unitStats));
			inputs.add(new JScrollPane(wepStats));
//			inputs.add(useDefault);
			inputs.add(done);
			
			
			add(pageTitle, BorderLayout.PAGE_START);
			add(calc, BorderLayout.PAGE_END);
			add(inputs);
		}
	}
	
	private class CreateElementsPanel extends JPanel {
		
		public CreateElementsPanel() {
			setLayout(new BorderLayout());

			int minLength = 8;
			
			JPanel unitTypesDisplay = new JPanel(new GridLayout(Math.max(minLength, MakerManager.unitTypes.size()), 1));
			for (int q = 0; q < MakerManager.unitTypes.size(); q++) {
				UnitType type = MakerManager.unitTypes.get(q);
				JButton button = new JButton(type.getName());
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchToPanel(new EditUnitTypePanel(type), VIEW);
					}
				});
				unitTypesDisplay.add(button);
			}
			int numLeft = minLength - MakerManager.unitTypes.size();
			for (int q = 0; q < numLeft; q++) {
				unitTypesDisplay.add(new JLabel());
			}
			JButton addUnitType = new JButton("Add Unit Type");
			addUnitType.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String name = JOptionPane.showInputDialog("Give a name to the unit type");
					try {
						if (name != null) {
							name = validateInputName(name, "Name");
							MakerManager.unitTypes.add(new UnitType(name));
							switchToPanel(new CreateElementsPanel(), VIEW);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			JPanel fullUnitTypesDisplay = new JPanel(new BorderLayout());
			fullUnitTypesDisplay.add(new JLabel("Unit Types"), BorderLayout.NORTH);
			fullUnitTypesDisplay.add(new JScrollPane(unitTypesDisplay));
			fullUnitTypesDisplay.add(addUnitType, BorderLayout.SOUTH);
			
			JPanel weaponTypesDisplay = new JPanel(new GridLayout(Math.max(minLength, MakerManager.weaponTypes.size()), 1));
			for (int q = 0; q < MakerManager.weaponTypes.size(); q++) {
				WeaponType type = MakerManager.weaponTypes.get(q);
				JButton button = new JButton(type.getName());
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						switchToPanel(new EditWeaponTypePanel(type), VIEW);
					}
				});
				weaponTypesDisplay.add(button);
			}
			numLeft = minLength - MakerManager.weaponTypes.size();
			for (int q = 0; q < numLeft; q++) {
				weaponTypesDisplay.add(new JLabel());
			}
			JButton addWeaponType = new JButton("Add Weapon Type");
			addWeaponType.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String name = JOptionPane.showInputDialog("Give a name to the weapon type");
					try {
						if (name != null) {
							name = validateInputName(name, "Name");
							MakerManager.weaponTypes.add(new WeaponType(name));
							switchToPanel(new CreateElementsPanel(), VIEW);
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			JPanel fullWeaponTypesDisplay = new JPanel(new BorderLayout());
			fullWeaponTypesDisplay.add(new JLabel("Weapon Types"), BorderLayout.NORTH);
			fullWeaponTypesDisplay.add(new JScrollPane(weaponTypesDisplay));
			fullWeaponTypesDisplay.add(addWeaponType, BorderLayout.SOUTH);
			
			JPanel unitClassesDisplay = new JPanel(new GridLayout(Math.max(minLength, MakerManager.unitClasses.size()), 1));
			for (int q = 0; q < MakerManager.unitClasses.size(); q++) {
				int idx = q;
				UnitClass type = MakerManager.unitClasses.get(q);
				JButton button = new JButton(type.getName());
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						String[] opts = {"View", "Set Promotion", "Delete"};
						int decision = JOptionPane.showOptionDialog(null,
								"Options for " + type.getName(), "OPTIONS",
								JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
								null, opts, opts[0]);
						if (decision == 0) {
							//TODO
						} else if (decision == 1) {
							//TODO
						} else if (decision == 2) {
							int decision2 = JOptionPane.showConfirmDialog(null, "Delete " + type.getName() + "?");
							if (decision2 == 0) {
								MakerManager.unitClasses.remove(idx);
								switchToPanel(new CreateElementsPanel(), VIEW);
							}
						}
					}
				});
				unitClassesDisplay.add(button);
			}
			numLeft = minLength - MakerManager.unitClasses.size();
			for (int q = 0; q < numLeft; q++) {
				unitClassesDisplay.add(new JLabel());
			}
			JButton addUnitClass = new JButton("Add Unit Class");
			addUnitClass.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new EditUnitClassPanel(), VIEW);
				}
			});
			JPanel fullUnitClassesDisplay = new JPanel(new BorderLayout());
			fullUnitClassesDisplay.add(new JLabel("Unit Classes"), BorderLayout.NORTH);
			fullUnitClassesDisplay.add(new JScrollPane(unitClassesDisplay));
			fullUnitClassesDisplay.add(addUnitClass, BorderLayout.SOUTH);
			
			JPanel weaponsDisplay = new JPanel(new GridLayout(Math.max(minLength, MakerManager.weapons.size()), 1));
			for (int q = 0; q < MakerManager.weapons.size(); q++) {
				Weapon type = MakerManager.weapons.get(q);
				JButton button = new JButton(type.getName());
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO edit(type)
					}
				});
				weaponsDisplay.add(button);
			}
			numLeft = minLength - MakerManager.weapons.size();
			for (int q = 0; q < numLeft; q++) {
				weaponsDisplay.add(new JLabel());
			}
			JButton addWeapon = new JButton("Add Weapon");
			addWeapon.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new EditWeaponPanel(), VIEW);
				}
			});
			JPanel fullWeaponsDisplay = new JPanel(new BorderLayout());
			fullWeaponsDisplay.add(new JLabel("Weapons"), BorderLayout.NORTH);
			fullWeaponsDisplay.add(new JScrollPane(weaponsDisplay));
			fullWeaponsDisplay.add(addWeapon, BorderLayout.SOUTH);

			JPanel unitsDisplay = new JPanel(new GridLayout(Math.max(minLength, MakerManager.units.size()), 1));
			for (int q = 0; q < MakerManager.units.size(); q++) {
				Unit type = MakerManager.units.get(q);
				JButton button = new JButton(type.getName());
				button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO edit(type)
					}
				});
				unitsDisplay.add(button);
			}
			numLeft = minLength - MakerManager.units.size();
			for (int q = 0; q < numLeft; q++) {
				unitsDisplay.add(new JLabel());
			}
			JButton addUnit = new JButton("Add Unit");
			addUnit.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new EditUnitPanel(), VIEW);
				}
			});
			JPanel fullUnitsDisplay = new JPanel(new BorderLayout());
			fullUnitsDisplay.add(new JLabel("Units"), BorderLayout.NORTH);
			fullUnitsDisplay.add(new JScrollPane(unitsDisplay));
			fullUnitsDisplay.add(addUnit, BorderLayout.SOUTH);
			
			JButton campaign = new JButton("Campaign Elements >>>");
			campaign.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
				}
			});

			JPanel elementsArray = new JPanel(new GridLayout(1, 5));
			elementsArray.add(fullUnitTypesDisplay);
			elementsArray.add(fullWeaponTypesDisplay);
			elementsArray.add(fullUnitClassesDisplay);
			elementsArray.add(fullWeaponsDisplay);
			elementsArray.add(fullUnitsDisplay);
			
			add(elementsArray);
			add(campaign, BorderLayout.PAGE_END);
		}
	}
	
	private class EditUnitTypePanel extends JPanel {
		public EditUnitTypePanel(UnitType type) {
			setLayout(new BorderLayout());
			
			JPanel skillSelection = new JPanel(new GridLayout(15, 1));
			JCheckBox mount = new JCheckBox("Mount/Dismount", type.isMount());
			JCheckBox mountainWalk = new JCheckBox("Easy Mountain Walking", type.isMountainWalk());
			JCheckBox desertWalk = new JCheckBox("Easy Desert Walking", type.isDesertWalk());
			JCheckBox waterWalk = new JCheckBox("Easy Water Walking", type.isWaterWalk());
			JCheckBox flying = new JCheckBox("Flying", type.isFlying());
			JCheckBox seize = new JCheckBox("Seize Victory Point", type.isSeize());
			JCheckBox steal = new JCheckBox("Steal", type.isSteal());
			JCheckBox invigorate = new JCheckBox("Invigorate Ally", type.isInvigorate());
			JCheckBox aoeInvigorate = new JCheckBox("Invigorate All Adjacent Allies", type.isAoeInvigorate());
			JCheckBox aoeHeal = new JCheckBox("Auto-Heal All Adjacent Allies", type.isAoeHeal());
			JCheckBox astra = new JCheckBox("Astra (5 Hits)", type.isAstra());
			JCheckBox luna = new JCheckBox("Luna (Nullify Defense)", type.isLuna());
			JCheckBox sol = new JCheckBox("Sol (Drain HP)", type.isSol());
			JCheckBox shield = new JCheckBox("Shield (Nullify Attack)", type.isShield());
			JCheckBox kill = new JCheckBox("Kill (Auto-Kill)", type.isKill());
			skillSelection.add(mount);
			skillSelection.add(mountainWalk);
			skillSelection.add(desertWalk);
			skillSelection.add(waterWalk);
			skillSelection.add(flying);
			skillSelection.add(seize);
			skillSelection.add(steal);
			skillSelection.add(invigorate);
			skillSelection.add(aoeInvigorate);
			skillSelection.add(aoeHeal);
			skillSelection.add(astra);
			skillSelection.add(luna);
			skillSelection.add(sol);
			skillSelection.add(shield);
			skillSelection.add(kill);
			
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					type.setMount(mount.isSelected());
					type.setMountainWalk(mountainWalk.isSelected());
					type.setDesertWalk(desertWalk.isSelected());
					type.setWaterWalk(waterWalk.isSelected());
					type.setFlying(flying.isSelected());
					type.setSeize(seize.isSelected());
					type.setSteal(steal.isSelected());
					type.setInvigorate(invigorate.isSelected());
					type.setAoeInvigorate(aoeInvigorate.isSelected());
					type.setAoeHeal(aoeHeal.isSelected());
					type.setAstra(astra.isSelected());
					type.setLuna(luna.isSelected());
					type.setSol(sol.isSelected());
					type.setShield(shield.isSelected());
					type.setKill(kill.isSelected());
					switchToPanel(new CreateElementsPanel(), VIEW);
				}
			});
			
			add(new JLabel(type.getName()), BorderLayout.PAGE_START);
			add(skillSelection);
			add(confirm, BorderLayout.PAGE_END);
		}
	}
	
	private class EditWeaponTypePanel extends JPanel {
		public EditWeaponTypePanel(WeaponType type) {
			setLayout(new BorderLayout());
			
			JPanel display = new JPanel(new GridLayout(12, 2));
			display.add(new JLabel("Advantage:"));
			WeaponType[] list = new WeaponType[MakerManager.weaponTypes.size()];
			//Leave [0] null
			int min = 0;
			for (int q = 0; q < MakerManager.weaponTypes.size(); q++) {
				if (MakerManager.weaponTypes.get(q) == type) {
					min = 1;
					continue;
				}
				list[q + 1 - min] = MakerManager.weaponTypes.get(q);
			}
			JComboBox<WeaponType> choices = new JComboBox<>(list);
			for (int q = 0; q < list.length; q++) {
				if (list[q] == type.getAdvantage()) {
					choices.setSelectedIndex(q);
					break;
				}
			}
			display.add(choices);
			for (int q = 0; q < 22; q++) {
				display.add(new JLabel());
			}
			
			JButton confirm = new JButton("Confirm");
			confirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WeaponType w = (WeaponType) choices.getSelectedItem();
					type.setAdvantage(w);
					switchToPanel(new CreateElementsPanel(), VIEW);
				}
			});
			
			add(new JLabel(type.getName()), BorderLayout.PAGE_START);
			add(display);
			add(confirm, BorderLayout.PAGE_END);
		}
	}
	
	private class EditUnitClassPanel extends JPanel {
		public EditUnitClassPanel() {
			setLayout(new BorderLayout());
			
			JPanel namePanel = new JPanel(new GridLayout(1, 2));
			JTextField nameField = new JTextField();
			namePanel.add(new JLabel("Name (Required):"));
			namePanel.add(nameField);
			
			JPanel movePanel = new JPanel(new GridLayout(1, 2));
			JTextField moveField = new JTextField();
			movePanel.add(new JLabel("Movement (Required):"));
			movePanel.add(moveField);
			
			JPanel minStatsPanel = new JPanel(new GridLayout(1, (MakerManager.stats.size() * 2) + 3));
			minStatsPanel.add(new JLabel("<html>Minimum Stats:<br/><html>(Required)<br/>"));
			minStatsPanel.add(new JLabel("HP"));
			JTextField hpField = new JTextField();
			minStatsPanel.add(hpField);
			ArrayList<JTextField> minStatsFields = new ArrayList<>();
			for (int q = 0; q < MakerManager.stats.size(); q++) {
				Stat s = MakerManager.stats.get(q);
				minStatsPanel.add(new JLabel(s.getAbbreviation()));
				JTextField field = new JTextField();
				minStatsPanel.add(field);
				minStatsFields.add(field);
			}
			
			JPanel minGrowthsPanel = new JPanel(new GridLayout(1, (MakerManager.stats.size() * 2) + 3));
			minGrowthsPanel.add(new JLabel("<html>Minimum Growths:<br/><html>(Required)<br/>"));
			minGrowthsPanel.add(new JLabel("HP"));
			JTextField hpGrowthField = new JTextField();
			minGrowthsPanel.add(hpGrowthField);
			ArrayList<JTextField> minGrowthsFields = new ArrayList<>();
			for (int q = 0; q < MakerManager.stats.size(); q++) {
				Stat s = MakerManager.stats.get(q);
				minGrowthsPanel.add(new JLabel(s.getAbbreviation()));
				JTextField field = new JTextField();
				minGrowthsPanel.add(field);
				minGrowthsFields.add(field);
			}
			
			JPanel unitTypesPanel = new JPanel(new GridLayout(1, MakerManager.unitTypes.size() + 1));
			unitTypesPanel.add(new JLabel("Unit Types:"));
			ArrayList<JCheckBox> unitTypesBoxes = new ArrayList<>();
			for (int q = 0; q < MakerManager.unitTypes.size(); q++) {
				UnitType ut = MakerManager.unitTypes.get(q);
				JCheckBox field = new JCheckBox(ut.getName());
				unitTypesPanel.add(field);
				unitTypesBoxes.add(field);
			}
			
			JPanel promotionPanel = new JPanel(new GridLayout(1, 2));
			promotionPanel.add(new JLabel("Promotion:"));
			UnitClass[] list = new UnitClass[MakerManager.unitClasses.size() + 1];
			for (int q = 1; q < list.length; q++) {
				list[q] = MakerManager.unitClasses.get(q - 1);
			}
			JComboBox<UnitClass> classesBox = new JComboBox<>(list);
			promotionPanel.add(classesBox);
			
			JPanel weaponTypesPanel = new JPanel(new GridLayout(1, MakerManager.weaponTypes.size() + 1));
			weaponTypesPanel.add(new JLabel("Weapon Types:"));
			ArrayList<JCheckBox> weaponTypesBoxes = new ArrayList<>();
			for (int q = 0; q < MakerManager.weaponTypes.size(); q++) {
				WeaponType ut = MakerManager.weaponTypes.get(q);
				JCheckBox field = new JCheckBox(ut.getName());
				weaponTypesPanel.add(field);
				weaponTypesBoxes.add(field);
			}
			
			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new CreateElementsPanel(), VIEW);
				}
			});
			JButton finish = new JButton("Finish Class");
			finish.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						String name = validateInputName(nameField.getText(), "Name");
						int hp = parseValidDigitWithinBounds(hpField.getText(), 0, Integer.MAX_VALUE, "Minimum HP");
						int hpGrowth = parseValidDigitWithinBounds(hpGrowthField.getText(), 0, Integer.MAX_VALUE, "Minimum HP Growth");
						int movement = parseValidDigitWithinBounds(moveField.getText(), 0, Integer.MAX_VALUE, "Movement");
						int[] minStats = new int[minStatsFields.size()];
						int[] minGrowths = new int[minGrowthsFields.size()];
						for (int q = 0; q < minStats.length; q++) {
							Stat s = MakerManager.stats.get(q);
							JTextField statField = minStatsFields.get(q);
							JTextField growthField = minGrowthsFields.get(q);
							int stat = parseValidDigitWithinBounds(statField.getText(), 0, Integer.MAX_VALUE, "Min " + s.getName());
							minStats[q] = stat;
							int growth = parseValidDigitWithinBounds(growthField.getText(), 0, Integer.MAX_VALUE, "Min " + s.getName() + " Growth");
							minGrowths[q] = growth;
						}
						minStats[minStats.length - 1] = hp;
						minGrowths[minGrowths.length - 1] = hpGrowth;
						UnitClass newClass = new UnitClass(name, movement, minStats, minGrowths);

						for (int q = 0; q < unitTypesBoxes.size(); q++) {
							if (unitTypesBoxes.get(q).isSelected()) {
								newClass.getTypes().add(MakerManager.unitTypes.get(q));
							}
						}
						newClass.setPromotion((UnitClass)classesBox.getSelectedItem());
						for (int q = 0; q < weaponTypesBoxes.size(); q++) {
							if (weaponTypesBoxes.get(q).isSelected()) {
								newClass.getEquippableTypes().add(MakerManager.weaponTypes.get(q));
							}
						}
						
						MakerManager.unitClasses.add(newClass);
						switchToPanel(new CreateElementsPanel(), VIEW);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			
			JPanel inputsPanel = new JPanel(new GridLayout(7, 1));
			inputsPanel.add(namePanel);
			inputsPanel.add(movePanel);
			inputsPanel.add(new JScrollPane(minStatsPanel));
			inputsPanel.add(new JScrollPane(minGrowthsPanel));
			inputsPanel.add(new JScrollPane(unitTypesPanel));
			inputsPanel.add(promotionPanel);
			inputsPanel.add(new JScrollPane(weaponTypesPanel));
			
			JPanel buttons = new JPanel(new GridLayout(1, 2));
			buttons.add(cancel);
			buttons.add(finish);
			
			add(new JLabel("Creat Unit Class"), BorderLayout.PAGE_START);
			add(inputsPanel);
			add(buttons, BorderLayout.PAGE_END);
		}
	}
	
	private class EditWeaponPanel extends JPanel {
		public EditWeaponPanel() {
			setLayout(new BorderLayout());
			
			JPanel namePanel = new JPanel(new GridLayout(1, 2));
			JTextField nameField = new JTextField();
			namePanel.add(new JLabel("Name (Required):"));
			namePanel.add(nameField);
			
			JPanel weaponTypePanel = new JPanel(new GridLayout(1, 2));
			weaponTypePanel.add(new JLabel("Weapon Type (Required):"));
			WeaponType[] list = new WeaponType[MakerManager.weaponTypes.size()];
			for (int q = 0; q < list.length; q++) {
				list[q] = MakerManager.weaponTypes.get(q);
			}
			JComboBox<WeaponType> weaponTypesBox = new JComboBox<>(list);
			weaponTypePanel.add(weaponTypesBox);
			
			JPanel durabilityPanel = new JPanel(new GridLayout(1, 2));
			JTextField durabilityField = new JTextField();
			durabilityPanel.add(new JLabel("Durability (Required):"));
			durabilityPanel.add(durabilityField);
			
			JPanel proficiencyPanel = new JPanel(new GridLayout(1, 2));
			JTextField proficiencyField = new JTextField();
			proficiencyPanel.add(new JLabel("Proficiency Requirement (Required):"));
			proficiencyPanel.add(proficiencyField);

			JPanel rangePanel = new JPanel(new GridLayout(1, 2));
			rangePanel.add(new JLabel("Range (Required):"));
			JPanel rangeFields = new JPanel(new GridLayout(1, 3));
			JTextField minField = new JTextField();
			JTextField maxField = new JTextField();
			rangeFields.add(minField);
			rangeFields.add(new JLabel("~"));
			rangeFields.add(maxField);
			rangePanel.add(rangeFields);
			
			JPanel weaponStatsPanel = new JPanel(new GridLayout(1, (MakerManager.wepStats.size() * 2) + 1));
			weaponStatsPanel.add(new JLabel("<html>Weapon Stats<br/><html>(Required)<br/>"));
			ArrayList<JTextField> statsFields = new ArrayList<>();
			for (int q = 0; q < MakerManager.wepStats.size(); q++) {
				Stat s = MakerManager.wepStats.get(q);
				weaponStatsPanel.add(new JLabel(s.getName()));
				JTextField field = new JTextField();
				weaponStatsPanel.add(field);
				statsFields.add(field);
			}
			
			JPanel effectivenessPanel = new JPanel(new GridLayout(1, MakerManager.unitTypes.size() + 1));
			effectivenessPanel.add(new JLabel("Effective Against:"));
			ArrayList<JCheckBox> unitTypesBoxes = new ArrayList<>();
			for (int q = 0; q < MakerManager.unitTypes.size(); q++) {
				UnitType ut = MakerManager.unitTypes.get(q);
				JCheckBox box = new JCheckBox(ut.getName());
				effectivenessPanel.add(box);
				unitTypesBoxes.add(box);
			}
			
			JPanel bravePanel = new JPanel(new GridLayout(1, 2));
			bravePanel.add(new JLabel("Brave Effect?:"));
			JCheckBox braveBox = new JCheckBox();
			bravePanel.add(braveBox);
			
			JPanel statBoostsPanel = new JPanel(new GridLayout(1, (MakerManager.stats.size() * 2) + 1));
			statBoostsPanel.add(new JLabel("Stat Boosts"));
			ArrayList<JTextField> statBoostFields = new ArrayList<>();
			for (int q = 0; q < MakerManager.stats.size(); q++) {
				Stat s = MakerManager.stats.get(q);
				statBoostsPanel.add(new JLabel(s.getAbbreviation()));
				JTextField field = new JTextField();
				statBoostsPanel.add(field);
				statBoostFields.add(field);
			}
			
			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new CreateElementsPanel(), VIEW);
				}
			});
			JButton finish = new JButton("Finish Weapon");
			finish.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						String name = validateInputName(nameField.getText(), "Name");
						if (weaponTypesBox.getSelectedItem() == null) {
							throw new IllegalArgumentException("Weapon Type cannot be none");
						}
						WeaponType type = (WeaponType)weaponTypesBox.getSelectedItem();
						int durability = parseValidDigitWithinBounds(durabilityField.getText(), 1, Integer.MAX_VALUE, "Durability");
						int prof = parseValidDigitWithinBounds(proficiencyField.getText(), 0, Integer.MAX_VALUE, "Proficiency");
						int minRange = parseValidDigitWithinBounds(minField.getText(), 1, Integer.MAX_VALUE, "Min Range");
						int maxRange = parseValidDigitWithinBounds(maxField.getText(), minRange, Integer.MAX_VALUE, "Max Range");
						int[] stats = new int[statsFields.size()];
						for (int q = 0; q < stats.length; q++) {
							String s = MakerManager.wepStats.get(q).getAbbreviation();
							stats[q] = parseValidDigitWithinBounds(statsFields.get(q).getText(), 0, Integer.MAX_VALUE, s);
						}
						Weapon newWeapon = new Weapon(name, type, durability, prof, minRange, maxRange, stats, MakerManager.stats.size());
						
						for (int q = 0; q < unitTypesBoxes.size(); q++) {
							if (unitTypesBoxes.get(q).isSelected()) {
								newWeapon.getEffective().add(MakerManager.unitTypes.get(q));
							}
						}
						newWeapon.setBrave(braveBox.isSelected());
						for (int q = 0; q < statBoostFields.size(); q++) {
							if (!statBoostFields.get(q).getText().equals("")) {
								String s = MakerManager.stats.get(q).getName();
								newWeapon.getUnitStatsEffects()[q]
										= parseValidDigitWithinBounds(statBoostFields.get(q).getText(), Integer.MIN_VALUE, Integer.MAX_VALUE, s + " Boost");
							}
						}
						MakerManager.weapons.add(newWeapon);
						switchToPanel(new CreateElementsPanel(), VIEW);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			
			JPanel inputsPanel = new JPanel(new GridLayout(9, 1));
			inputsPanel.add(namePanel);
			inputsPanel.add(weaponTypePanel);
			inputsPanel.add(durabilityPanel);
			inputsPanel.add(proficiencyPanel);
			inputsPanel.add(rangePanel);
			inputsPanel.add(new JScrollPane(weaponStatsPanel));
			inputsPanel.add(new JScrollPane(effectivenessPanel));
			inputsPanel.add(bravePanel);
			inputsPanel.add(new JScrollPane(statBoostsPanel));
			
			JPanel buttons = new JPanel(new GridLayout(1, 2));
			buttons.add(cancel);
			buttons.add(finish);
			
			add(new JLabel("Create Weapon"), BorderLayout.PAGE_START);
			add(inputsPanel);
			add(buttons, BorderLayout.PAGE_END);
		}
	}
	
	private class EditUnitPanel extends JPanel {
		public EditUnitPanel() {
			setLayout(new BorderLayout());
			
			JPanel namePanel = new JPanel(new GridLayout(1, 2));
			JTextField nameField = new JTextField();
			namePanel.add(new JLabel("Name (Required):"));
			namePanel.add(nameField);
			
			JPanel classPanel = new JPanel(new GridLayout(1, 2));
			classPanel.add(new JLabel("Class (Required):"));
			UnitClass[] list = new UnitClass[MakerManager.unitClasses.size()];
			for (int q = 0; q < MakerManager.unitClasses.size(); q++) {
				list[q] = MakerManager.unitClasses.get(q);
			}
			JComboBox<UnitClass> classesBox = new JComboBox<>(list);
			classPanel.add(classesBox);
			
			JPanel statsPanel = new JPanel(new GridLayout(1, (MakerManager.stats.size() * 2) + 3));
			statsPanel.add(new JLabel("<html>Stats:<br/><html>(Required)<br/>"));
			statsPanel.add(new JLabel("HP"));
			JTextField hpField = new JTextField();
			statsPanel.add(hpField);
			ArrayList<JTextField> statsFields = new ArrayList<>();
			for (int q = 0; q < MakerManager.stats.size(); q++) {
				Stat s = MakerManager.stats.get(q);
				statsPanel.add(new JLabel(s.getAbbreviation()));
				JTextField field = new JTextField();
				statsPanel.add(field);
				statsFields.add(field);
			}
			
			JPanel growthsPanel = new JPanel(new GridLayout(1, (MakerManager.stats.size() * 2) + 3));
			growthsPanel.add(new JLabel("<html>Growths:<br/><html>(Required)<br/>"));
			growthsPanel.add(new JLabel("HP"));
			JTextField hpGrowthField = new JTextField();
			growthsPanel.add(hpGrowthField);
			ArrayList<JTextField> growthsFields = new ArrayList<>();
			for (int q = 0; q < MakerManager.stats.size(); q++) {
				Stat s = MakerManager.stats.get(q);
				growthsPanel.add(new JLabel(s.getAbbreviation()));
				JTextField field = new JTextField();
				growthsPanel.add(field);
				growthsFields.add(field);
			}

			JPanel levelPanel = new JPanel(new GridLayout(1, 2));
			JTextField levelField = new JTextField();
			levelPanel.add(new JLabel("Level (Required):"));
			levelPanel.add(levelField);
			
			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					switchToPanel(new CreateElementsPanel(), VIEW);
				}
			});
			JButton finish = new JButton("Finish Unit");
			finish.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					try {
						String name = validateInputName(nameField.getText(), "Name");
						if (classesBox.getSelectedItem() == null) {
							throw new IllegalArgumentException("Class cannot be none");
						}
						UnitClass unitClass = (UnitClass) classesBox.getSelectedItem();
						int hp = parseValidDigitWithinBounds(hpField.getText(), 1, Integer.MAX_VALUE, "HP");
						int hpGrowth = parseValidDigitWithinBounds(hpGrowthField.getText(), 0, Integer.MAX_VALUE, "HP Growth");
						int[] stats = new int[MakerManager.stats.size()];
						int[] growths = new int[MakerManager.stats.size()];
						for (int q = 0; q < MakerManager.stats.size(); q++) {
							Stat s = MakerManager.stats.get(q);
							stats[q] = parseValidDigitWithinBounds(statsFields.get(q).getText(), 0, Integer.MAX_VALUE, s.getName());
							growths[q] = parseValidDigitWithinBounds(growthsFields.get(q).getText(), 0, Integer.MAX_VALUE, s.getName() + " Growth");
						}
						int level = parseValidDigitWithinBounds(levelField.getText(), 1, Integer.MAX_VALUE, "Level");
						Unit newUnit = new Unit(name, unitClass, hp, hpGrowth, stats, growths, MakerManager.inventorySize, level);

						MakerManager.units.add(newUnit);
						switchToPanel(new CreateElementsPanel(), VIEW);
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					}
				}
			});
			
			JPanel inputsPanel = new JPanel(new GridLayout(5, 1));
			inputsPanel.add(namePanel);
			inputsPanel.add(classPanel);
			inputsPanel.add(new JScrollPane(statsPanel));
			inputsPanel.add(new JScrollPane(growthsPanel));
			inputsPanel.add(levelPanel);
			
			JPanel buttons = new JPanel(new GridLayout(1, 2));
			buttons.add(cancel);
			buttons.add(finish);
			
			add(new JLabel("Create Unit"), BorderLayout.PAGE_START);
			add(inputsPanel);
			add(buttons, BorderLayout.PAGE_END);
		}
	}
	
}
