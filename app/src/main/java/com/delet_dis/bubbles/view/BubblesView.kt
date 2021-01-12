package com.delet_dis.bubbles.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.collision.shapes.EdgeShape
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.*


class BubblesView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context!!, attrs, defStyleAttr) {
  private var mWorld: World? = null

  private val mWorldWidth = 8f
  private var mWorldHeight = 0f
  private var mDpm = 0f
  private fun init() {
    setWillNotDraw(false)
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    mDpm = w / mWorldWidth
    mWorldHeight = h / mDpm
  }

  private fun getScreenRotate(radians: Float): Float {
    return (radians * 180f / Math.PI).toFloat()
  }

  private fun getRealCenter(view: View): PointF {
    return PointF((view.x + view.width / 2) / mDpm,
            (view.y + view.height / 2) / mDpm)
  }

  private fun getRealRadius(view: View): Float {
    return view.width / mDpm / 2
  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    createNewWorld()
  }

  private fun createNewWorld() {
    mWorld = World(Vec2(0f, 9.8f))
    createBorder()
    for (i in 0 until childCount) {
      val circleView = getChildAt(i)
      val circleBody = createCircleBody(circleView, i)
      circleView.tag = circleBody
    }
  }

  private fun createCircleBody(view: View, index: Int): Body {
    val bodyDef = BodyDef()
    bodyDef.type = BodyType.DYNAMIC
    val realCenter = getRealCenter(view)
    bodyDef.position[realCenter.x] = realCenter.y
    val shape = CircleShape()
    shape.radius = getRealRadius(view)
    val fixture = FixtureDef()
    fixture.shape = shape
    fixture.friction = 0.03f
    fixture.restitution = 0.5f
    fixture.density = 0.3f
    val body = mWorld!!.createBody(bodyDef)
    body.createFixture(fixture)
    body.applyForceToCenter(Vec2((index + 1).toFloat(), (index + 2).toFloat()))
    return body
  }


  private fun createBorder() {
    val bodyDef = BodyDef()
    val groundBody = mWorld!!.createBody(bodyDef)
    val edge = EdgeShape()
    val boxShapeDef = FixtureDef()
    boxShapeDef.shape = edge

    edge[Vec2(0f, 0f)] = Vec2(mWorldWidth, 0f)
    groundBody.createFixture(boxShapeDef)

    edge[Vec2(0f, 0f)] = Vec2(0f, mWorldHeight)
    groundBody.createFixture(boxShapeDef)

    edge[Vec2(mWorldWidth, 0f)] = Vec2(mWorldWidth, mWorldHeight)
    groundBody.createFixture(boxShapeDef)

    edge[Vec2(0f, mWorldHeight)] = Vec2(mWorldWidth, mWorldHeight)
    groundBody.createFixture(boxShapeDef)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    mWorld!!.step(1f / 60, 8, 8)
    for (i in 0 until childCount) {
      val circleView = getChildAt(i)
      if (circleView.tag != null && circleView.tag is Body) {
        val body = circleView.tag as Body
        circleView.x = body.position.x * mDpm - circleView.width / 2
        circleView.y = body.position.y * mDpm - circleView.height / 2
        circleView.rotation = getScreenRotate(body.angle)
      }
    }
    invalidate()
  }

  fun onSensorChanged(x: Float, y: Float) {
    val realX = -x
    val realY = y + 1
    mWorld!!.gravity = Vec2(realX, realY)
  }

  override fun performClick(): Boolean {
    return super.performClick()
  }

  init {
    init()
  }
}