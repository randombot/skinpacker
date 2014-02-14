package com.randombot.theme;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.Node;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2;
import com.badlogic.gdx.tools.imagepacker.TexturePacker2.Settings;

import es.eucm.ead.mockup.core.view.ui.Panel;
import es.eucm.ead.mockup.core.view.ui.ToolBar;

public class HoloThemePacker implements ApplicationListener {

	private final Color CLEAR_COLOR = new Color(0.705f, 0.7411f, 0.875f, 1f);

	/**
	 * If true an UI showing the final skin will be shown.
	 */
	private final boolean DISPLAY_RESULT = true;
	
	//Establish here if you want your resulted project to be copied to some other directory...	 
	private final boolean COPY = false;
	private final String dirCpy = "YOUR_COPY_DIR_HERE";

	private final String[] sizes = new String[] { "xhdpi" };
	private final String[] colours = new String[] { "light" };	

	private final String fontName = "dfpop"; 
	private final String color = "dark"; 
	private final String size = "xhdpi"; 

	private ArrayList<String> icNames;
	private String auxDir;
	private Stage ui;

	public void create() {		

		compile(color, size);	// ONLY XHDPI for now...

		if(!DISPLAY_RESULT) return;
		//After compiling display the result...		
		ui = new Stage();

		Skin s = new Skin(Gdx.files.internal("HoloResources/result/" + sizes[0] + "/holo-dark-xhdpi.json"));
		Table uiTable = new Table(s);

		uiTable.add(new Label("Label", s));
		uiTable.row();
		uiTable.add(new CheckBox("CheckBox", s));
		uiTable.row();

		ScrollPane scroll = new ScrollPane(uiTable, s);
		scroll.setFillParent(true);
		ui.addActor(scroll);

		showOtherIcons(s, uiTable);

		// Buttons
		uiTable.row();	
		TextButton dlButton = new TextButton("Download", s);
		uiTable.add(dlButton).fill().expandX();
		uiTable.add(new TextButton("Thin Font Button", s, "default-thin")).fill().expandX();
		uiTable.add(new ImageButton(s)).fill().expandX();

		// Toggle Buttons
		uiTable.row();
		uiTable.add(new TextButton("Click Me\nGo", s, "toggle")).fill().expandX();
		uiTable.add(new TextButton("Thin Font Button", s, "toggle-thin"))
		.fill().expandX();
		uiTable.add(new ImageButton(s, "toggle")).fill().expandX();

		// SelectBox
		uiTable.row();
		String[] testSelect = new String[] { "Options", "Settings", "Network", "Personal Settings" };
		uiTable.add(new SelectBox(testSelect, s)).fill().expandX();
		uiTable.add(new SelectBox(testSelect, s, "default-thin"));

		// Slider
		uiTable.row();
		uiTable.add(new Slider(0, 100, 1, false, s, "left-horizontal")).fill().expandX().colspan(3);

		// List & Tree
		uiTable.row();
		uiTable.add(new List(testSelect, s));
		Tree t = new Tree(s);
		Node l1 = new Node(new Label("Level 1", s, "default-opaque"));
		l1.add(new Node(new Label("Level 1.1", s, "default-opaque")));
		l1.add(new Node(new Label("Level 1.2", s, "default-opaque")));
		t.add(l1);
		t.add(new Node(new Label("Level 2", s, "default-opaque")));
		uiTable.add(t).fill().expandX().colspan(2);

		//Panel, toolbar...
		uiTable.row();
		uiTable.add("Panel");
		uiTable.add(new Panel(s)).fill().expand().colspan(3);
		uiTable.row();
		uiTable.add("ToolBar");
		uiTable.add(new ToolBar(s)).fill().expand().colspan(3);

		CheckBox checkBox = new CheckBox("Check me", s);
		final Slider slider = new Slider(0, 10, 1, false, s);
		TextField textfield = new TextField("", s);
		textfield.setMessageText("Click here!");
		SelectBox dropdown = new SelectBox(
				new String[] {"Android", "Windows", "Linux", 
						"OSX","Android", "Windows", "Linux", 
						"OSX","Android", "Windows", "Linux", 
						"OSX","Android", "Windows", "Linux", 
						"OSX","Android", "Windows", "Linux", 
						"OSX","Android", "Windows", "Linux", 
						"OSX","Android", "Windows", "Linux"}, s);
		List list = new List(dropdown.getItems(), s);
		ScrollPane scrollPane2 = new ScrollPane(list, s);
		scrollPane2.setFlickScroll(false);
		Window window = new Window("Test Window", s);
		window.add(checkBox);
		window.add(slider).minWidth(100).fillX().colspan(3);
		window.row();
		window.add(dropdown);
		window.add(textfield).minWidth(100).expandX().fillX().colspan(3);

		uiTable.row();
		uiTable.add(window).fill().expand().colspan(3);

		// Radio and Checkbox
		uiTable.row();
		uiTable.add(new CheckBox("CheckBox", s)).fill().expandX();
		uiTable.add(new CheckBox("Radio Button", s, "default-radio")).fill()
		.expandX();
		uiTable.add(new TextField("Text ����-# .>", s)).fill().expandX();

		final Dialog compileDone = new Dialog("Compiling done...", s, "dialog");
		compileDone.row();
		compileDone.add(new Label("Compiling done", s)).expandX().fill().center();
		TextButton hideDialog = new TextButton("OK", s);
		hideDialog.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				compileDone.setVisible(false);
			}
		});
		compileDone.row().fill().expandX();
		compileDone.add(hideDialog).fill().expandX();
		compileDone.row().fill().expand();
		compileDone.setFillParent(true);
		ui.addActor(compileDone);

		uiTable.row();
		uiTable.add(new Label("Skin Manager", s)).fill().expandX()
		.colspan(3);
		uiTable.row();
		final SelectBox sb_col = new SelectBox(colours, s);
		uiTable.add(sb_col).fill().expandX();
		final SelectBox sb_sizes = new SelectBox(sizes, s);
		uiTable.add(sb_sizes).fill().expandX();
		TextButton tb_compile = new TextButton("Nothing [CompileSkin]", s);
		uiTable.add(tb_compile).fill().expandX();
		TextButton tb_compile_all = new TextButton("Nothing [CompAll]", s);
		uiTable.row();
		uiTable.add(tb_compile_all).fill().expandX().colspan(3);

		Gdx.input.setInputProcessor(ui);
	}

	/**
	 * Packs the images and prepares the skin to be used...
	 */
	public void compile(String color, String size) {

		this.auxDir = Gdx.files.internal("").file().getAbsolutePath() + File.separator;

		// empty tmp folder
		Gdx.files.absolute(this.auxDir+"HoloResources/tmp").emptyDirectory();

		// copy created files
		for (FileHandle f : Gdx.files.absolute(this.auxDir+"HoloResources/created/drawable-" + size).list()) {
			f.copyTo(Gdx.files.absolute(this.auxDir+"HoloResources/tmp/" + f.name()));
		}

		// copy JSON file updating content
		Writer w = Gdx.files.absolute(this.auxDir+
				"HoloResources/result/" + sizes[0] + File.separator + "holo-" + color + "-" + size + ".json")
				.writer(false);
		BufferedReader br = new BufferedReader(new InputStreamReader(Gdx.files
				.absolute(this.auxDir+"HoloResources/created/stuff/holo.json").read()));

		icNames = new ArrayList<String>();
		String name;
		for (FileHandle f : Gdx.files.absolute(this.auxDir+"HoloResources/created/drawable-" + size).list()) {
			name = f.nameWithoutExtension();
			if (name.contains("ic_")){
				icNames.add(name);
			}
		}	

		try {
			String line = br.readLine();
			while (line != null) {
				if (line.contains("<icon>")){		
					for (int i = 0; i < icNames.size(); ++i){
						String aux = line.replace("<icon>", icNames.get(i));						
						w.append(aux + "\n");	
					}
				} else {			
					line = line.replace("<col>", color);
					line = line.replace("<size>", size);
					w.append(line + "\n");	
				}
				line = br.readLine();		
			}
			br.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// copy Fonts
		Gdx.files.absolute(this.auxDir+"HoloResources/created/stuff/" + fontName + "-" + size + ".fnt")
		.copyTo(Gdx.files.absolute(this.auxDir+"HoloResources/result/" + sizes[0] + File.separator + fontName + "-" + size + ".fnt"));

		Gdx.files.absolute(this.auxDir+"HoloResources/created/stuff/" + fontName + "-" + size + ".png")
		.copyTo(Gdx.files.absolute(this.auxDir+"HoloResources/tmp/" + fontName + "-" + size + ".png"));

		Gdx.files.absolute(this.auxDir+"HoloResources/created/stuff/" + fontName + "-Thin-" + size + ".fnt")
		.copyTo(Gdx.files.absolute(this.auxDir+"HoloResources/result/" + sizes[0] + File.separator + fontName + "-Thin-" + size + ".fnt"));

		Gdx.files.absolute(this.auxDir+"HoloResources/created/stuff/" + fontName + "-Thin-" + size + ".png")
		.copyTo(Gdx.files.absolute(this.auxDir+"HoloResources/tmp/" + fontName + "-Thin-" + size + ".png"));

		Gdx.files.absolute(this.auxDir+"HoloResources/created/stuff/" + fontName + "-Toolbar-" + size + ".fnt")
		.copyTo(Gdx.files.absolute(this.auxDir+"HoloResources/result/" + sizes[0] + File.separator + fontName + "-Toolbar-" + size + ".fnt"));

		Gdx.files.absolute(this.auxDir+"HoloResources/created/stuff/" + fontName + "-Toolbar-" + size + ".png")
		.copyTo(Gdx.files.absolute(this.auxDir+"HoloResources/tmp/" + fontName + "-Toolbar-" + size + ".png"));		

		Settings set = new TexturePacker2.Settings();
		set.filterMag = TextureFilter.Linear;
		set.filterMin = TextureFilter.Linear;
		set.pot = false;
		set.maxHeight = 1024;
		set.maxWidth = 1024;
		set.paddingX = 4;
		set.paddingY = 4;

		// Pack all pngs	
		TexturePacker2.process(set, 
				auxDir + "/HoloResources/tmp",
				auxDir + "/HoloResources/result/" + sizes[0], 
				"holo-" + color + "-" + size);

		// empty tmp folder
		Gdx.files.absolute(this.auxDir+"HoloResources/tmp").emptyDirectory();

		//Copy the result to some folder... maybe your project? 
		cpy();
		
		//We're done!
	}

	public void cpy(){
		if(!this.COPY) return;

		Gdx.files.absolute(dirCpy).emptyDirectory();
		Gdx.files.absolute(this.auxDir+"HoloResources/result/" + sizes[0])
		.copyTo(Gdx.files.absolute(dirCpy));		
	}

	private void showOtherIcons(Skin s, Table uiTable){
		// Toggle Buttons
		uiTable.row();		

		int numIc = icNames.size();
		for (int i = 0; i < numIc; ++i){
			ImageButton ib = new ImageButton(s, icNames.get(i));		
			uiTable.add(ib).fill().expandX();			
			if (i % 3 == 0) { uiTable.row(); }
		}
	}

	public void render() {
		Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b, CLEAR_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ui.act();
		ui.draw();
	}

	public void pause() { }
	public void resume() { }
	public void dispose() { ui.dispose(); }	
	public void resize(int width, int height) { ui.setViewport(width, height, true); }
}
