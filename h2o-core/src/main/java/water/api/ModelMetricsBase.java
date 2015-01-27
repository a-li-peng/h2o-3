package water.api;

import hex.Model;
import hex.ModelMetrics;
import water.api.KeyV1.FrameKeyV1;
import water.api.KeyV1.ModelKeyV1;
import water.fvec.Frame;

/**
 * Base Schema for individual instances of ModelMetrics objects.
 */
public abstract class ModelMetricsBase<I extends ModelMetrics, S extends ModelMetricsBase<I, S>> extends Schema<I, S> {
  // InOut fields
  @API(help="The model used for this scoring run.", direction=API.Direction.INOUT)
  // public KeyV1<Key<Model>> model;
  public ModelKeyV1 model;

  @API(help="The checksum for the model used for this scoring run.", direction=API.Direction.INOUT)
  public long model_checksum;

  @API(help="The frame used for this scoring run.", direction=API.Direction.INOUT)
  public FrameKeyV1 frame;
  // public FrameV2 frame; // TODO: should use a base class!

  @API(help="The checksum for the frame used for this scoring run.", direction=API.Direction.INOUT)
  public long frame_checksum;

  // Output fields
  @API(help="The category (e.g., Clustering) for the model used for this scoring run.", values={"Unknown", "Binomial", "Multinomial", "Regression", "Clustering"}, direction=API.Direction.OUTPUT)
  public Model.ModelCategory model_category ;

  @API(help="The duration in mS for this scoring run.", direction=API.Direction.OUTPUT)
  public long duration_in_ms;

  @API(help="The time in mS since the epoch for the start of this scoring run.", direction=API.Direction.OUTPUT)
  public long scoring_time;

  @API(help="Predictions Frame.", direction=API.Direction.OUTPUT)
  public FrameV2 predictions;

  @Override public S fillFromImpl(ModelMetrics modelMetrics) {
    // If we're copying in a Model we need a ModelSchema of the right class to fill into.
    Model m = modelMetrics.model();
    if( m != null ) {
      this.model = new ModelKeyV1(m._key);
      this.model_category = m._output.getModelCategory();
      this.model_checksum = m.checksum();
    }

    // If we're copying in a Frame we need a Frame Schema of the right class to fill into.
    Frame f = modelMetrics.frame();
    if (null != f) { //true == f.getClass().getSuperclass().getGenericSuperclass() instanceof ParameterizedType
      this.frame = new FrameKeyV1(f._key);
      this.frame_checksum = f.checksum();
    }

    return (S) this;
  }
}
