package kotterknife

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ViewTest {

    @Test
    fun testCast() {
        class Example(context: Context) : FrameLayout(context) {
            val name: TextView by bindView(1)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(textViewWithId(1))
        assertNotNull(example.name)
    }

    @Test
    fun testFindCached() {
        class Example(context: Context) : FrameLayout(context) {
            val name: View by bindView(1)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(viewWithId(1))
        assertNotNull(example.name)
        example.removeAllViews()
        assertNotNull(example.name)
    }

    @Test
    fun testOptional() {
        class Example(context: Context) : FrameLayout(context) {
            val present: View? by bindOptionalView(1)
            val missing: View? by bindOptionalView(2)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(viewWithId(1))
        assertNotNull(example.present)
        assertNull(example.missing)
    }

    @Test
    fun testOptionalCached() {
        class Example(context: Context) : FrameLayout(context) {
            val present: View? by bindOptionalView(1)
            val missing: View? by bindOptionalView(2)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(viewWithId(1))
        assertNotNull(example.present)
        assertNull(example.missing)
        example.removeAllViews()
        example.addView(viewWithId(2))
        assertNotNull(example.present)
        assertNull(example.missing)
    }

    @Test
    fun testMissingFails() {
        class Example(context: Context) : FrameLayout(context) {
            val name: TextView? by bindView(1)
        }

        val example = Example(InstrumentationRegistry.getContext())
        try {
            example.name
        } catch (e: IllegalStateException) {
            assertEquals("View ID 1 for 'name' not found.", e.message)
        }
    }

    @Test
    fun testList() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindViews(1, 2, 3)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(viewWithId(1))
        example.addView(viewWithId(2))
        example.addView(viewWithId(3))
        assertNotNull(example.name)
        assertEquals(3, example.name.count())
    }

    @Test
    fun testListCaches() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindViews(1, 2, 3)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(viewWithId(1))
        example.addView(viewWithId(2))
        example.addView(viewWithId(3))
        assertNotNull(example.name)
        assertEquals(3, example.name.count())
        example.removeAllViews()
        assertNotNull(example.name)
        assertEquals(3, example.name.count())
    }

    @Test
    fun testListMissingFails() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindViews(1, 2, 3)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(viewWithId(1))
        example.addView(viewWithId(3))
        try {
            example.name
        } catch (e: IllegalStateException) {
            assertEquals("View ID 2 for 'name' not found.", e.message)
        }
    }

    @Test
    fun testOptionalList() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindOptionalViews(1, 2, 3)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(viewWithId(1))
        example.addView(viewWithId(3))
        assertNotNull(example.name)
        assertEquals(2, example.name.count())
    }

    @Test
    fun testOptionalListCaches() {
        class Example(context: Context) : FrameLayout(context) {
            val name: List<TextView> by bindOptionalViews(1, 2, 3)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(viewWithId(1))
        example.addView(viewWithId(3))
        assertNotNull(example.name)
        assertEquals(2, example.name.count())
        example.removeAllViews()
        assertNotNull(example.name)
        assertEquals(2, example.name.count())
    }

    @Test
    fun testReset() {
        class Example(context: Context) : FrameLayout(context) {
            val name: View? by bindOptionalView(1)
        }

        val example = Example(InstrumentationRegistry.getContext())
        example.addView(viewWithId(1))
        assertNotNull(example.name)
        example.removeAllViews()
        KotterKnife.reset(example)
        assertNull(example.name)
    }

    private fun viewWithId(id: Int): View {
        val view = View(InstrumentationRegistry.getContext())
        view.id = id
        return view
    }

    private fun textViewWithId(id: Int): View {
        val view = TextView(InstrumentationRegistry.getContext())
        view.id = id
        return view
    }
}
