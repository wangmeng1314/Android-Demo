package com.example.service;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import android.content.Context;
import android.database.MergeCursor;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class ChartService {

	private GraphicalView mGraphicalView;
	private XYMultipleSeriesDataset multipleSeriesDataset;// 数据集容器
	private XYMultipleSeriesRenderer multipleSeriesRenderer;// 渲染器容器
	private XYSeries mSeries;// 单条曲线数据集
	private XYSeriesRenderer mRenderer;// 单条曲线渲染器
	private Context context;
	private int addX = -1, addY;
	int[] xv = new int[200];
	int[] yv = new int[200];
	private int length;

	public ChartService(Context context) {
		this.context = context;
	}

	/**
	 * 获取图表
	 * 
	 * @return
	 */
	public GraphicalView getGraphicalView() {
		mGraphicalView = ChartFactory.getCubeLineChartView(context,
				multipleSeriesDataset, multipleSeriesRenderer, 0.1f);
		return mGraphicalView;
	}

	/**
	 * 获取数据集，及xy坐标的集合
	 * 
	 * @param curveTitle
	 */
	public void setXYMultipleSeriesDataset(String curveTitle) {
		multipleSeriesDataset = new XYMultipleSeriesDataset();
		mSeries = new XYSeries(curveTitle);
		multipleSeriesDataset.addSeries(mSeries);
	}

	/**
	 * 获取渲染器
	 * 
	 * @param maxX
	 *            x轴最大值
	 * @param maxY
	 *            y轴最大值 * @param chartTitle * 曲线的标题
	 * @param xTitle
	 *            x轴标题
	 * @param yTitle
	 *            y轴标题
	 * @param axeColor
	 *            坐标轴颜色
	 * @param labelColor
	 *            标题颜色
	 * @param curveColor
	 *            曲线颜色
	 * @param gridColor
	 *            网格颜色
	 */
	public void setXYMultipleSeriesRenderer(double maxX, double maxY,
			String chartTitle, String xTitle, String yTitle, int axeColor,
			int labelColor, int curveColor, int gridColor) {
		multipleSeriesRenderer = new XYMultipleSeriesRenderer();
		if (chartTitle != null) {
			multipleSeriesRenderer.setChartTitle(chartTitle);
		}
		multipleSeriesRenderer.setXTitle(xTitle);
		multipleSeriesRenderer.setYTitle(yTitle);
		multipleSeriesRenderer.setRange(new double[] { 0, maxX, 20, maxY });// xy轴的范围
		multipleSeriesRenderer.setLabelsColor(labelColor);
		multipleSeriesRenderer.setXLabels(10);
		multipleSeriesRenderer.setYLabels(10);
		multipleSeriesRenderer.setXLabelsAlign(Align.RIGHT);
		multipleSeriesRenderer.setYLabelsAlign(Align.RIGHT);
		multipleSeriesRenderer.setAxisTitleTextSize(10);
		multipleSeriesRenderer.setChartTitleTextSize(10);
		multipleSeriesRenderer.setLabelsTextSize(10);
		multipleSeriesRenderer.setLegendTextSize(10);
		multipleSeriesRenderer.setPointSize(2f);// 曲线描点尺寸
		multipleSeriesRenderer.setFitLegend(true);
		multipleSeriesRenderer.setMargins(new int[] { 0, 0, 0, 0 });
		multipleSeriesRenderer.setShowGrid(true);
		multipleSeriesRenderer.setAxesColor(axeColor);
		multipleSeriesRenderer.setGridColor(gridColor);
		multipleSeriesRenderer.setBackgroundColor(Color.WHITE);// 背景色
		multipleSeriesRenderer.setMarginsColor(Color.WHITE);// 边距背景色，默认背景色为黑色，这里修改为白色
		multipleSeriesRenderer.setZoomEnabled(false, false);
		multipleSeriesRenderer.setAntialiasing(true);
		multipleSeriesRenderer.setBackgroundColor(Color.BLACK);
		multipleSeriesRenderer.setClickEnabled(false);
		multipleSeriesRenderer.setShowLegend(false);
		multipleSeriesRenderer.setExternalZoomEnabled(false);
		mRenderer = new XYSeriesRenderer();
		mRenderer.setColor(curveColor);
		mRenderer.setFillPoints(true);
		mRenderer.setPointStyle(PointStyle.CIRCLE);// 描点风格，可以为圆点，方形点等等
		mRenderer.setLineWidth(1);
		multipleSeriesRenderer.addSeriesRenderer(mRenderer);
	}

	/**
	 * 根据新加的数据，更新曲线，只能运行在主线程
	 * 
	 * @param add
	 * 
	 */
	public void updateChart(int add) {
		// 需要加入的点集
		addX = 0;
		addY = add;
		// 移除旧的点集
		multipleSeriesDataset.removeSeries(mSeries);
		// 判断当前点集中到底有多少个点?
		length = mSeries.getItemCount();
		// 屏幕中最多只能容纳200个点
		if (length > 200) {
			length = 200;
		}
		for (int i = 0; i < length; i++) {
			xv[i] = (int) mSeries.getX(i) + 1;
			yv[i] = (int) mSeries.getY(i);
		}
		// clear
		mSeries.clear();
		// 动态展示
		mSeries.add(addX, addY);
		for (int k = 0; k < length; k++) {
			mSeries.add(xv[k], yv[k]);
		}

		multipleSeriesDataset.addSeries(mSeries);
		// mSeries.add(x, y);
		mGraphicalView.repaint();// 此处也可以调用invalidate()
		// mGraphicalView.invalidate();
	}

	/**
	 * 添加新的数据，多组，更新曲线，只能运行在主线程
	 * 
	 * @param xList
	 * @param yList
	 */
	public void updateChart(List<Double> xList, List<Double> yList) {
		for (int i = 0; i < xList.size(); i++) {
			mSeries.add(xList.get(i), yList.get(i));
		}
		mGraphicalView.repaint();// 此处也可以调用invalidate()
	}
}