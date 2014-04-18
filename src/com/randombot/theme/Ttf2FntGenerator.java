/**
 * eAdventure is a research project of the
 *    e-UCM research group.
 *
 *    Copyright 2005-2014 e-UCM research group.
 *
 *    You can access a list of all the contributors to eAdventure at:
 *          http://e-adventure.e-ucm.es/contributors
 *
 *    e-UCM is a research group of the Department of Software Engineering
 *          and Artificial Intelligence at the Complutense University of Madrid
 *          (School of Computer Science).
 *
 *          CL Profesor Jose Garcia Santesmases 9,
 *          28040 Madrid (Madrid), Spain.
 *
 *          For more info please visit:  <http://e-adventure.e-ucm.es> or
 *          <http://www.e-ucm.es>
 *
 * ****************************************************************************
 *
 *  This file is part of eAdventure
 *
 *      eAdventure is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU Lesser General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      eAdventure is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU Lesser General Public License for more details.
 *
 *      You should have received a copy of the GNU Lesser General Public License
 *      along with eAdventure.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.randombot.theme;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.tools.bmfont.BitmapFontWriter;

public class Ttf2FntGenerator {
	private static final String TAG = "Ttf2FntGenerator";
	private int pageSize;

	public Ttf2FntGenerator() {
		pageSize = 512; // size of atlas pages for font pngs
	}

	/**
	 * Will load font from file. If that fails, font will be generated and saved
	 * to file.
	 * 
	 * @param fontFile
	 *            the actual font (.otf, .ttf)
	 * @param fontName
	 *            the name of the font, i.e. "arial-small", "arial-large",
	 *            "monospace-10" This will be used for creating the font file
	 *            names
	 * @param fontSize
	 *            size of font when screen width equals referenceScreenWidth
	 */
	public void createFont(FileHandle fontFile, String fontName, int fontSize,
			FileHandle destiny) {
		generateFiles(fontName, fontFile, fontSize, pageSize, pageSize, destiny);
	}

	/**
	 * Convenience method for generating a font, and then writing the fnt and
	 * png files. Writing a generated font to files allows the possibility of
	 * only generating the fonts when they are missing, otherwise loading from a
	 * previously generated file.
	 * 
	 * @param fontFile
	 * @param fontSize
	 * @param destiny
	 */
	private void generateFiles(String fontName, FileHandle fontFile,
			int fontSize, int pageWidth, int pageHeight, FileHandle destiny) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

		PixmapPacker packer = new PixmapPacker(pageWidth, pageHeight,
				Pixmap.Format.RGBA8888, 2, false);
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.packer = packer;
		param.characters = FreeTypeFontGenerator.DEFAULT_CHARS;
		param.size = fontSize;
		param.flip = false;

		FreeTypeFontGenerator.FreeTypeBitmapFontData fontData = generator
				.generateData(param);

		saveFontToFile(fontData, fontSize, fontName, packer, destiny);
		generator.dispose();
		packer.dispose();
	}

	private void saveFontToFile(FreeTypeBitmapFontData data, int fontSize,
			String fontName, PixmapPacker packer, FileHandle destiny) {
		FileHandle fontFile = Gdx.files.absolute(destiny.file()
				.getAbsolutePath() + File.separator + fontName + ".fnt"); // .fnt
																			// path
		
		
		BitmapFontWriter.setOutputFormat(BitmapFontWriter.OutputFormat.Text);

		String[] pageRefs = BitmapFontWriter.writePixmaps(packer.getPages(),
				destiny, fontName); // png dir path
		Gdx.app.debug(TAG, String.format(
				"Saving font [%s]: fontfile: %s, pixmapDir: %s\n", fontName,
				fontFile, destiny));
		// here we must add the png dir to the page refs
		for (int i = 0; i < pageRefs.length; i++) {
			pageRefs[i] = pageRefs.length == 1 ? (fontName + ".png") : (fontName
					+ "_" + i);
		}
		BitmapFontWriter.writeFont(data, pageRefs, fontFile,
				new BitmapFontWriter.FontInfo(fontName, fontSize), 1, 1);
	}

	/**
	 * Set the width and height of the png files to which the fonts will be
	 * saved. In the future it would be nice for page size to be automatically
	 * set to the optimal size by the font generator. In the mean time it must
	 * be set manually.
	 */
	public void setPageSize(int size) {
		pageSize = size;
	}

	public int getPageSize() {
		return pageSize;
	}
}
