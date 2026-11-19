# Task 4 – Event-Handling Forensics

## Why the faulty handler may not run
A listener method with the wrong signature/name does not override the interface method. For example, `mouseclick` is not `mouseClicked`. The correct listener must be registered with `addMouseListener`.

## Why the label may not update
The handler may update a local variable rather than the displayed JLabel, or fail to call `setText`. The repaired program updates the shared counter label after storing each point.

## Repainting diagnosis and repair
Drawing directly once in a mouse handler is temporary: minimize/restore, uncovering or resizing can clear the pixels. The program stores each click as a `Point`, calls `repaint()`, and draws all stored points in `paintComponent(Graphics)`.

`@Override` asks the compiler to verify that the method overrides a superclass/interface method. A misspelled method then produces a compile-time error.

## Point list vs BufferedImage
A `List<Point>` stores O(k) points for k clicks and redraws O(k) dots during repaint. A BufferedImage stores a raster canvas of O(width × height) pixels and can make repainting fast by drawing the image, but consumes memory proportional to pixel dimensions and needs image lifecycle management.
