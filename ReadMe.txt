This is 3D a game engine made primarily using the LWJGL library. The LWJGL
basically allows for OpenGL to be used in Java.

The way the demo is set up right now, the camera rotation is controlled with
the arrow keys and the movement with wasd. Pressing 'e' will cause some of the
shapes to move.

Some notes about this demo:
1. Pretty much all the settings that control rendering (which shapes to render,
where, how big, how many, etc.) can be found in the ToRender class which can
be found in src/engine/object/ToRender.java.
2. Because of issues with slick (another library I'm using) the textures will
output a warning when rendered. This warning can be ignored and doesn't cause
any issues (as far as I'm aware). I've made it so that the warning doesn't
output to the console (because it was clogging everything up). You can change
that by deleting lines 48, 49 and 70 of the Material.java class
(src/engine/graphics/Material.java)
3. All textures should be in a PNG format (non-interlaced) and should be placed
in the resources/textures directory.
4. While textures can be different sizes, I've found that 256x256 pixels is the
best and that's the size everything was tested with.
5. The camera movement speed and its rotation speed can be changed on line 18 of
the Camera.java class (src/engine/objects/Camera.java)