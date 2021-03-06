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
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class SkinPacker implements ApplicationListener {

	private final Color CLEAR_COLOR = new Color(0.705f, 0.7411f, 0.875f, 1f);

	/**
	 * If true an UI showing the final skin will be shown.
	 */
	private final boolean DISPLAY_RESULT = true;

	// Establish here if you want your resulted project to be copied to some
	// other directory...
	private final boolean COPY = true;
	//private final String dirCpy = "Z:/UNI/Workspace_ead_Rotaru/ead/assets/skins/mockup";
	private final String dirCpy = "/home/cristian/hlocal/hlocal_maven/ead/assets/skins/mockup";
	private final String[] sizes = new String[] { "xhdpi" };
	private final String[] colours = new String[] { "light" };

	final int FONT_SIZE = 49;
	private final String fontName = "font";

	private ArrayList<String> icNames;
	private String auxDir;
	private Stage ui;

	public void create() {

		compile(); // ONLY XHDPI for now...

		if (!DISPLAY_RESULT)
			return;
		// After compiling display the result...
		ui = new Stage(new ExtendViewport(1280, 800));

		Skin s = new Skin(Gdx.files.internal("HoloResources/result/" + sizes[0]
				+ "/skin.json"));
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
		uiTable.add(new TextButton("Thin Font Button", s, "default-thin"))
		.fill().expandX();
		uiTable.add(new ImageButton(s)).fill().expandX();

		// Toggle Buttons
		uiTable.row();
		uiTable.add(new TextButton("Click Me\nGo", s, "toggle")).fill()
		.expandX();
		uiTable.add(new TextButton("Thin Font Button", s, "toggle-thin"))
		.fill().expandX();
		uiTable.add(new ImageButton(s, "toggle")).fill().expandX();

		// SelectBox
		uiTable.row();
		String[] testSelect = new String[] { "Options", "Settings", "Network",
		"Personal Settings" };
		SelectBox<String> s1, s2;
		uiTable.add(s1 = new SelectBox<String>(s)).fill().expandX();
		uiTable.add(s2 = new SelectBox<String>(s, "default-thin"));
		s1.setItems(testSelect);
		s2.setItems(testSelect);
		// Slider
		uiTable.row();
		uiTable.add(new Slider(0, 100, 1, false, s, "left-horizontal")).fill()
		.expandX().colspan(3);

		// List & Tree
		uiTable.row();
		List<String> ll1;
		uiTable.add(ll1 = new List<String>(s));
		ll1.setItems(testSelect);
		Tree t = new Tree(s);
		Node l1 = new Node(new Label("Level 1", s, "default-opaque"));
		l1.add(new Node(new Label("Level 1.1", s, "default-opaque")));
		l1.add(new Node(new Label("Level 1.2", s, "default-opaque")));
		t.add(l1);
		t.add(new Node(new Label("Level 2", s, "default-opaque")));
		uiTable.add(t).fill().expandX().colspan(2);

		// Panel, toolbar...
		uiTable.row();
		uiTable.add("...Panel...");
		uiTable.row();
		uiTable.add("...ToolBar...");
		CheckBox checkBox = new CheckBox("Check me", s);
		final Slider slider = new Slider(0, 10, 1, false, s);
		TextField textfield = new TextField("", s);
		textfield.setMessageText("Click here!");
		SelectBox<String> dropdown = new SelectBox<String>(s);
		dropdown.setItems(new String[] { "Android", "Windows", "Linux", "OSX",
				"Android", "Windows", "Linux", "OSX", "Android", "Windows",
				"Linux", "OSX", "Android", "Windows", "Linux", "OSX",
				"Android", "Windows", "Linux", "OSX", "Android", "Windows",
				"Linux", "OSX", "Android", "Windows", "Linux" });
		List<String> list = new List<String>(s);
		list.setItems(dropdown.getItems());
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
		compileDone.add(new Label("Compiling done", s)).expandX().fill()
		.center();
		TextButton hideDialog = new TextButton("OK", s);
		hideDialog.addListener(new ClickListener() {
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
		uiTable.add(new Label("Skin Manager", s)).fill().expandX().colspan(3);
		uiTable.row();
		final SelectBox<String> sb_col = new SelectBox<String>(s);
		sb_col.setItems(colours);
		uiTable.add(sb_col).fill().expandX();
		final SelectBox<String> sb_sizes = new SelectBox<String>(s);
		sb_sizes.setItems(sizes);
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
	public void compile() {

		this.auxDir = Gdx.files.internal("").file().getAbsolutePath()
				+ File.separator;
		createFont();

		// empty tmp folder
		Gdx.files.absolute(this.auxDir + "HoloResources/tmp").emptyDirectory();

		// copy created files
		for (FileHandle f : Gdx.files.absolute(
				this.auxDir + "HoloResources/created/drawable").list()) {
			f.copyTo(Gdx.files.absolute(this.auxDir + "HoloResources/tmp/"
					+ f.name()));
		}

		// copy JSON file updating content
		Writer w = Gdx.files.absolute(
				this.auxDir + "HoloResources/result/" + sizes[0]
						+ File.separator + "skin.json").writer(false);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				Gdx.files.absolute(
						this.auxDir + "HoloResources/created/stuff/holo.json")
						.read()));

		icNames = new ArrayList<String>();
		String name;
		for (FileHandle f : Gdx.files.absolute(
				this.auxDir + "HoloResources/created/drawable").list()) {
			name = f.nameWithoutExtension();
			if (name.contains("ic_")) {
				icNames.add(name);
			}
		}

		try {
			String line = br.readLine();
			while (line != null) {
				w.append(line + "\n");
				line = br.readLine();
			}
			br.close();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// copy Fonts
		Gdx.files.absolute(
				this.auxDir + "HoloResources/created/stuff/" + fontName
				+ ".fnt").copyTo(
						Gdx.files.absolute(this.auxDir + "HoloResources/result/"
								+ sizes[0] + File.separator + fontName + ".fnt"));

		FileHandle dir = Gdx.files.absolute(this.auxDir
				+ "HoloResources/created/stuff/");
		for (FileHandle child : dir.list()) {
			if (child.name().startsWith(fontName)) {
				child.copyTo(Gdx.files.absolute(this.auxDir
						+ "HoloResources/tmp/" + child.name()));
			}
		}

		Settings set = new TexturePacker.Settings();
		set.filterMag = TextureFilter.Linear;
		set.filterMin = TextureFilter.MipMapLinearNearest;
		set.pot = true;
		set.maxHeight = 1024;
		set.maxWidth = 1024;
		set.paddingX = 2;
		set.paddingY = 2;
		set.limitMemory = false;

		// Pack all pngs
		TexturePacker.process(set, auxDir + "/HoloResources/tmp", auxDir
				+ "/HoloResources/result/" + sizes[0], "skin");

		// empty tmp folder
		Gdx.files.absolute(this.auxDir + "HoloResources/tmp").emptyDirectory();

		// Copy the result to some folder... maybe your project?
		cpy();

		// We're done!
	}

	private void createFont() {
		// empty previous font files
		FileHandle dir = Gdx.files.absolute(this.auxDir
				+ "HoloResources/created/stuff/");
		for (FileHandle child : dir.list()) {
			if (child.name().startsWith(fontName)) {
				child.delete();
			}
		}
		// create .fnt from .ttf
		new Ttf2FntGenerator().createFont(
				Gdx.files.absolute(this.auxDir + "HoloResources"
						+ File.separator + "ttf" + File.separator
						+ "Grill Sans MT.ttf"),
						fontName,
						FONT_SIZE,
						Gdx.files.absolute(this.auxDir + "HoloResources"
								+ File.separator + "created" + File.separator + "stuff"
								+ File.separator));
	}

	public void cpy() {
		if (!this.COPY)
			return;

		Gdx.files.absolute(dirCpy).emptyDirectory();
		Gdx.files.absolute(this.auxDir + "HoloResources/result/" + sizes[0])
		.copyTo(Gdx.files.absolute(dirCpy));
	}

	private void showOtherIcons(Skin s, Table uiTable) {
		// Toggle Buttons
		uiTable.row();

		int numIc = icNames.size();
		for (int i = 0; i < numIc; ++i) {
			Button ib = new Button(s);
			ib.add(new Image(s, icNames.get(i)));
			uiTable.add(ib).fill().expand();
			if (i % 3 == 0) {
				uiTable.row();
			}
		}
	}

	public void render() {
		Gdx.gl.glClearColor(CLEAR_COLOR.r, CLEAR_COLOR.g, CLEAR_COLOR.b,
				CLEAR_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ui.act();
		ui.draw();
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
		ui.dispose();
	}

	public void resize(int width, int height) {
		ui.getViewport().update(width, height, true);
	}
}
