module BADProject {
	requires javafx.graphics;
	requires javafx.controls;
	requires java.sql;
	requires javafx.base;
	requires jfxtras.labs;
	
	opens database;
	opens model;
	opens view;
}