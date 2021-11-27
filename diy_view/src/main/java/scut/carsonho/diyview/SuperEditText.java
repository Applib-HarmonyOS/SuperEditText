package scut.carsonho.diyview;

import ohos.agp.colors.RgbColor;
import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.Component.DrawTask;
import ohos.agp.components.Component.FocusChangedListener;
import ohos.agp.components.Component.TouchEventListener;
import ohos.agp.components.Text.TextObserver;
import ohos.agp.components.TextField;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.components.element.ShapeElement;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import ohos.global.resource.ResourceManager;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Rect;
import ohos.media.image.common.Size;
import ohos.multimodalinput.event.TouchEvent;
import com.hmos.compat.utils.AttrUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

/**
 * Created by Carson_Ho on 17/8/4.
 */
public class SuperEditText extends TextField implements TextObserver,
        FocusChangedListener, TouchEventListener, DrawTask {
    private static final HiLogLabel HILOG_LABEL = new HiLogLabel(0, 0, "SuperEditText");

    /*
     * Define attribute variables
     * */
    // Brush
    private Paint mPaint;

    // Delete icon
    private Element icdelete;

    // Icon on the left (clicked & not clicked)
    private Element icleftclick;
    private Element icleftunclick;

    // Dividing line variable
    // When clicked & color not clicked
    private int lineColorclick;
    private int lineColorunclick;
    private int color;
    private int linePosition;

    /**
     * SuperEditText Constructor.
     *
     *  @param context - context for SuperEditText constructor
     *
     */
    public SuperEditText(Context context) {
        super(context);
        addTextObserver(this);
        setFocusChangedListener(this);
        setTouchEventListener(this);
        addDrawTask(this);
    }

    /**
     * SuperEditText Constructor.
     *
     *  @param context - context for SuperEditText constructor
     *  @param attrs - attributes
     *
     */
    public SuperEditText(Context context, AttrSet attrs) {
        super(context, attrs);
        init(context, attrs);
        addTextObserver(this);
        setFocusChangedListener(this);
        setTouchEventListener(this);
        addDrawTask(this);
    }

    /**
     * SuperEditText Constructor.
     *
     *  @param context - context for SuperEditText constructor
     *  @param attrs - attributes
     *  @param defStyleAttr - defStyle attribute
     *
     */
    public SuperEditText(Context context, AttrSet attrs, String defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
        addTextObserver(this);
        setFocusChangedListener(this);
        setTouchEventListener(this);
        addDrawTask(this);
    }

    /**
     *  Step 1: Initialize the properties.
     */
    private void init(Context context, AttrSet attrs) {
        try {
            // Initialize the icon on the left (clicked & not clicked)
            // a. Click the icon on the left side of the status
            // 1. Get resource ID
            int icleftclickResId = attrs.getAttr("ic_left_click").isPresent()
                    ? attrs.getAttr("ic_left_click").get().getIntegerValue()
                    : ResourceTable.Media_ic_left_click;
            // 2. Get icon id based on getElementFromResId
            icleftclick = getElementFromResId(getContext(), icleftclickResId).get();
            // 3. Set icon size
            //  The starting point of the icon on the left (x,y), the width and height of the icon on the left (px)
            int leftx = AttrUtils.getIntFromAttr(attrs, "left_x", 0);
            int lefty = AttrUtils.getIntFromAttr(attrs, "left_y", 0);
            int leftwidth = AttrUtils.getIntFromAttr(attrs, "left_width", 60);
            int leftheight = AttrUtils.getIntFromAttr(attrs, "left_height", 60);
            icleftclick.setBounds(leftx, lefty, leftwidth, leftheight);
            // Element.setBounds(x, y, width, height) = set the initial position, width,
            // and height information of the Element
            // x = the starting point of the component on the X-axis of the container,
            // y = the starting point of the component on the Y-axis of the container,
            // width = the length of the component, height = the height of the component

            // b. Did not click on the icon on the left of the status
            // 1. Get resource ID
            int icleftunclickResId = attrs.getAttr("ic_left_unclick").isPresent()
                    ? attrs.getAttr("ic_left_unclick").get().getIntegerValue()
                    : ResourceTable.Media_ic_left_unclick;
            icleftunclick = getElementFromResId(getContext(), icleftunclickResId).get();
            icleftunclick.setBounds(leftx, lefty, leftwidth, leftheight);
            // Initialize delete icon
            // 1. Get resource ID
            // Delete icon resource ID
            int icdeleteResId = attrs.getAttr("ic_delete").isPresent()
                    ? attrs.getAttr("ic_delete").get().getIntegerValue()
                    : ResourceTable.Media_delete;
            // 2. Get icon id based on getElementFromResId
            icdelete = getElementFromResId(getContext(), icdeleteResId).get();
            // 3. Set icon size
            // The starting point of the Delete icon (x,y), the width and height of the Delete icon (px)
            int deletex = AttrUtils.getIntFromAttr(attrs, "delete_x", 0);
            int deletey = AttrUtils.getIntFromAttr(attrs, "delete_y", 0);
            int deletewidth = AttrUtils.getIntFromAttr(attrs, "delete_width", 60);
            int deleteheight = AttrUtils.getIntFromAttr(attrs, "delete_height", 60);
            icdelete.setBounds(deletex, deletey, deletewidth, deleteheight);

            // Set the picture on the left & right side of the EditText
            // (only the picture on the left is available in the initial state))
            setAroundElements(icleftunclick, null, null, null);
            // Introduction to
            // setAroundElements (Element left, Element top, Element right, Element bottom)
            // Function: Set icons on the top, bottom, left, and right of
            // EditText (equivalent to ohos:element_left="" ohos:element_right="")
            //Remarks: The incoming Element object must have setBounds (x, y, width, height), that is,
            // the initial position, width, and height information must be set.
            // x: The starting point of the component on the X-axis of the container
            // y: the starting point of the component on the Y-axis of the container
            // width: The length of the component height: the height of the component
            // If you don't want to display it somewhere, set it to null

            // No need to set setBounds (x, y, width, height)
            // Initialize the cursor (color & thickness)
            // 1. Get resource ID
            int defaultColor = getContext().getResourceManager()
                    .getElement(ResourceTable.Color_cursor_default).getColor();
            int cursor = attrs.getAttr("cursor").isPresent()
                    ? attrs.getAttr("cursor").get().getColorValue().getValue() : defaultColor;
            ShapeElement shapeElement = new ShapeElement();
            shapeElement.setRgbColor(RgbColor.fromArgbInt(cursor));
            setCursorElement(shapeElement);

            // Initialize the dividing line (color, thickness, position)
            // 1. Set brush
            mPaint = new Paint();
            // Thickness of dividing line
            mPaint.setStrokeWidth(2.0f);
            // 2. Set the color of the dividing line (use hexadecimal codes, such as #333, #8e8e8e)
            // Default= blue#1296db
            int lineColorClickdefault = context.getResourceManager()
                    .getElement(ResourceTable.Color_lineColor_click).getColor();
            int lineColorunClickdefault = context.getResourceManager()
                    .getElement(ResourceTable.Color_lineColor_unclick).getColor();
            lineColorclick = AttrUtils.getColorFromAttr(attrs, "lineColor_click", lineColorClickdefault);
            lineColorunclick = AttrUtils.getColorFromAttr(attrs, "lineColor_unclick", lineColorunClickdefault);
            color = lineColorunclick;
            // 2. Get icon resources based on resource ID (converted into Drawable objects)
            Color hmosColor = SuperEditText.changeParamToColor(lineColorunclick);
            // Default color of dividing line = gray
            mPaint.setColor(hmosColor);
            Color hmosColor1 = SuperEditText.changeParamToColor(color);
            // Font default color = gray
            setTextColor(hmosColor1);
            // 3. Dividing line position
            linePosition = AttrUtils.getIntFromAttr(attrs, "linePosition", 1);
            // Eliminate the built-in underscore
            setBackground(null);
        } catch (Exception exception) {
            HiLog.error(HILOG_LABEL, "SuperEditText init exception " + exception.getMessage());
        }
    }

    private static Optional<PixelMapElement> getElementFromResId(Context context, int resid)
            throws IOException, NotExistException {
        ResourceManager resourceManager = context.getResourceManager();
        if (resourceManager == null) {
            return Optional.empty();
        }
        Optional<PixelMapElement> element = Optional.empty();
        if (resid != 0) {
            try {
                Resource resource = resourceManager.getResource(resid);
                element = prepareElement(resource);
            } catch (IOException | NotExistException e) {
                HiLog.error(HILOG_LABEL, "SuperEditText getElementFromResId e " + e.getMessage());
            }
        }
        return Optional.of(element.get());
    }

    private static Optional<PixelMapElement> prepareElement(Resource resource) {
        Optional<PixelMap> pixelMap = preparePixelMap(resource);
        if (pixelMap.isPresent()) {
            PixelMapElement pixelMapElement = new PixelMapElement(pixelMap.get());
            return Optional.of(pixelMapElement);
        }
        return Optional.empty();
    }

    private static Optional<PixelMap> preparePixelMap(Resource resource) {
        PixelMap decodePixelMap = null;
        try {
            if (resource != null) {
                byte[] bytes = readBytes(resource);
                resource.close();
                if (bytes == null) {
                    return Optional.empty();
                }
                ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
                ImageSource imageSource = ImageSource.create(bytes, srcOpts);
                if (imageSource == null) {
                    HiLog.error(HILOG_LABEL, " preparePixelMap imageSource is null ");
                }
                ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
                decodingOptions.desiredSize = new Size(0, 0);
                decodingOptions.desiredRegion = new Rect(0, 0, 0, 0);
                decodingOptions.desiredPixelFormat = PixelFormat.ARGB_8888;
                if (imageSource != null) {
                    decodePixelMap = imageSource.createPixelmap(decodingOptions);
                }
            }
        } catch (IOException e) {
            HiLog.error(HILOG_LABEL, " preparePixelMap, ioexception : " + e.getMessage());
        }
        return Optional.ofNullable(decodePixelMap);
    }

    private static byte[] readBytes(Resource resource) {
        if (resource != null) {
            final int bufferSize = 1024;
            final int ioEnd = -1;

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] bytes = new byte[bufferSize];
            byte[] bytesArray = new byte[0];
            while (true) {
                try {
                    int readLen = resource.read(bytes, 0, bufferSize);
                    if (readLen == ioEnd) {
                        bytesArray = output.toByteArray();
                        return bytesArray;
                    }
                    output.write(bytes, 0, readLen);
                } catch (IOException e) {
                    break;
                } finally {
                    try {
                        output.close();
                    } catch (IOException e) {
                        HiLog.error(HILOG_LABEL, " readBytes close output failed ");
                    }
                }
            }
            return  bytesArray;
        } else {
            return new byte[]{};
        }
    }

    /**
     * Method of overriding EditText itself: onTextChanged().
     * Call time: when the content of the input box changes
     */
    @Override
    public void onTextUpdated(String text, int start, int lengthBefore, int lengthAfter) {
        setDeleteIconVisible(hasFocus() && text.length() > 0, hasFocus());
        //hasFocus() returns whether the focus of the EditText is obtained, that is, whether it is selected
        // setDeleteIconVisible()= Determine whether to display the delete icon based on
        // whether the incoming one is selected & whether there is input ->> Follow 1
    }

    /**
     * Method of overriding the EditText itself: ONFOCUSCHANGED().
     * Call time: when the focus changes
     */
    @Override
    public void onFocusChange(Component component, boolean focused) {
        setDeleteIconVisible(focused && length() > 0, focused);
        // focused = whether to get focus
        // Also determine whether to display the delete icon according to SETDELETEICONVISIBLE()- >> Follow 1
    }

    /**
     * Function: Set the delete icon area to "click to empty the search box content".
     * Principle: When the position of the finger raised is in the area of the delete icon,
     * it is deemed to have clicked the Delete icon = empty the search box content
     */
    @Override
    public boolean onTouchEvent(Component component, TouchEvent event) {
        // Principle: When the position of the finger raised is in the area of the delete icon,
        // it is deemed that the delete icon has been clicked = Clear the contents of the search box
        if (event.getAction() == TouchEvent.PRIMARY_POINT_UP) {
            Element drawable = icdelete;
            if (drawable != null && event.getPointerPosition(0).getX()
                    <= (getWidth() - getPaddingRight()) && event.getPointerPosition(0).getX()
                    >= (getWidth() - getPaddingRight() - drawable.getBounds().getWidth())) {
                String param = SuperEditText.changeParamToString("");
                // Description of judgment conditions
                // event.getX(): the position coordinates when lifting
                //getWidth(): the width of the control
                //getPaddingRight(): Delete the distance from the right edge of
                // the icon icon to the right edge of the EditText control
                // That is: getWidth()-getPaddingRight() = Delete the coordinates of the right edge of the icon = X1
                // getWidth() - getPaddingRight() - drawable.getBounds().width()
                // = Delete the coordinates of the left edge of the icon = X2
                // So the area between X1 and X2 = the area where the icon is deleted
                // When the finger is raised in the area where the icon is deleted (X2=<event.getX()<=X1),
                // it is deemed to have clicked the Delete icon = empty the search box content
                setText(param);
            }
        }
        return true;
    }

    /**
     *Follow 1.
     * Function: determine whether to display the delete icon & set the color of the dividing line
     */
    private void setDeleteIconVisible(boolean deleteVisible, boolean leftVisible) {
        setAroundElements(leftVisible ? icleftclick : icleftunclick, null, deleteVisible ? icdelete : null, null);
        color = leftVisible ? lineColorclick : lineColorunclick;
        Color hmosColor = SuperEditText.changeParamToColor(color);
        setTextColor(hmosColor);
        invalidate();
    }

    /**
     * Function: draw dividing lines.
     */
    @Override
    public void onDraw(Component component, ohos.agp.render.Canvas canvas) {
        Color hmosColor = SuperEditText.changeParamToColor(color);
        mPaint.setColor(hmosColor);
        Color hmosColor1 = SuperEditText.changeParamToColor(color);
        setTextColor(hmosColor1);
        // Draw dividing lines
        // Need to consider: When the input length exceeds the input box, the drawn line needs to follow the extension
        // Solution: The length of the line = the length of the control + the length after extension
        // Get the length after extension
        int x = this.getScrollValue(Component.AXIS_X);
        // Get control length
        int w = this.getEstimatedWidth();
        // When passing in parameters, the length of the line = the length of the control + the length after extension
        canvas.drawLine(0, this.getEstimatedHeight() - linePosition,
                w + x, this.getEstimatedHeight() - linePosition, mPaint);
    }

    public static Color changeParamToColor(int color) {
        return new Color(color);
    }

    public static String changeParamToString(CharSequence charSequence) {
        return charSequence.toString();
    }

}
