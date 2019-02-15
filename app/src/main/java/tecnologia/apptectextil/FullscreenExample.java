package tecnologia.apptectextil;

import android.support.annotation.LayoutRes;
/*
import com.jjoe64.graphview_demos.examples.AddSeriesAtRuntime;
import com.jjoe64.graphview_demos.examples.AdvancedBarGraph;
import com.jjoe64.graphview_demos.examples.AdvancedLineGraph;
import com.jjoe64.graphview_demos.examples.BaseExample;
import com.jjoe64.graphview_demos.examples.CustomLabelsGraph;
import com.jjoe64.graphview_demos.examples.Dates;
import com.jjoe64.graphview_demos.examples.FixedFrame;
import com.jjoe64.graphview_demos.examples.HelloWorld;
import com.jjoe64.graphview_demos.examples.MultipleBarGraph;
import com.jjoe64.graphview_demos.examples.NoLabelsGraph;
import com.jjoe64.graphview_demos.examples.RealtimeScrolling;
import com.jjoe64.graphview_demos.examples.ScalingX;
import com.jjoe64.graphview_demos.examples.ScalingXY;
import com.jjoe64.graphview_demos.examples.ScrollingX;
import com.jjoe64.graphview_demos.examples.SecondScaleGraph;
import com.jjoe64.graphview_demos.examples.SimpleBarGraph;
import com.jjoe64.graphview_demos.examples.SimpleLineGraph;
import com.jjoe64.graphview_demos.examples.SimplePointsGraph;
import com.jjoe64.graphview_demos.examples.SnapshotShareGraph;
import com.jjoe64.graphview_demos.examples.StaticLabelsGraph;
import com.jjoe64.graphview_demos.examples.StylingColors;
import com.jjoe64.graphview_demos.examples.StylingLabels;
import com.jjoe64.graphview_demos.examples.TapListenerGraph;
import com.jjoe64.graphview_demos.examples.TitlesExample;
import com.jjoe64.graphview_demos.examples.UniqueLegendLineGraph;*/

/**
 * Created by jonas on 10.09.16.
 */
public enum FullscreenExample {
    HELLO_WORLD(R.layout.fullscreen_example_simple, HelloWorld.class)
    ;

    public static final String ARG_ID = "FEID";
    static final String URL_PREFIX = "https://github.com/jjoe64/GraphView-Demos/blob/master/app/src/main/java/com/jjoe64/graphview_demos/examples/";

    public final @LayoutRes int contentView;
    public final Class<? extends BaseExample> exampleClass;
    public final String url;

    FullscreenExample(@LayoutRes int contentView, Class<? extends BaseExample> exampleClass) {
        this.contentView = contentView;
        this.exampleClass = exampleClass;
        this.url = URL_PREFIX+exampleClass.getSimpleName()+".java";
    }
}
