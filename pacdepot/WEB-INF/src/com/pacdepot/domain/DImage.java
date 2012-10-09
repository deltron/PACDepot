package com.pacdepot.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pacdepot.exception.ImageException;

public class DImage {
	private byte[] _buf = null;
	private int _height = -1;
	private int _width = -1;
	private String _contentType = null;

	public DImage(byte[] buf, String contentType) {
		if (buf.length > 0 && contentType != null && contentType.length() > 0) {
			_contentType = contentType;
			_buf = buf;
		}
	}

	public DImage(byte[] buf, String contentType, int width, int height) throws ImageException {
		try {
			if (buf.length > 0
				&& contentType != null
				&& contentType.length() > 0
				&& width > 0
				&& height > 0) {
				_contentType = contentType;
				_width = width;
				_height = height;
				_buf = buf;
			} else
				throw new ImageException();
		} catch (Exception e) {
			throw new ImageException(e);
		}
	}

	public DImage(File file, String contentType, int width, int height)
		throws FileNotFoundException, IOException {
		_contentType = contentType;
		_width = width;
		_height = height;

		FileInputStream in = new FileInputStream(file);
		List blocks = new ArrayList();
		int blocklen = 0;
		int totallen = 0;

		while ((blocklen = in.available()) != 0) {
			byte[] block = new byte[blocklen];
			in.read(block);
			blocks.add(block);
			totallen += blocklen;
		}

		_buf = new byte[totallen];
		int offset = 0;
		Iterator iterator = blocks.iterator();

		while (iterator.hasNext()) {
			byte[] block = (byte[]) iterator.next();
			for (int i = 0; i < block.length; i++)
				_buf[offset + i] = block[i];
			offset += block.length;
		}

		System.out.println("INFO    Image created, size = " + totallen / 1024 + "KB");
	}

	public String getContentType() {
		return _contentType;
	}
	public int getHeight() {
		return _height;
	}
	public int getWidth() {
		return _width;
	}
	public byte[] getBytes() {
		return _buf;
	}
}
