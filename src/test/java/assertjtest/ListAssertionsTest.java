package assertjtest;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import org.assertj.core.api.SoftAssertions;
import org.junit.Test;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class ListAssertionsTest {
    /*
    includes stuff from iterable
     */

    private static final List<ProductProjection> list = Fixtures.productProjectionList();

    @Test
    public void name1() {
        final List<String> names = list.stream().map(p -> p.getId()).collect(toList());
        assertThat(names).isEqualTo(asList("foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91"));
/*
org.junit.ComparisonFailure: expected:<["[foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91]"]> but was:<["[8665d91a-eab0-45a8-a378-e6603d66d084",
"347caac1-daa2-4e31-bc95-e237ba1e2b91",
"f7edccd2-95d7-4fea-b511-ae5b3e309c7d",
"ff0c34c3-bd33-4991-89f5-dfa96b69b26c",
"461e2e52-50c9-4d77-a2f2-1cb77b601556]"]>
 */
    }

    @Test
    public void name2() {
        final List<String> names = list.stream().map(p -> p.getId()).collect(toList());
        assertThat(names).contains("foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91");
/*
 <["8665d91a-eab0-45a8-a378-e6603d66d084",
    "347caac1-daa2-4e31-bc95-e237ba1e2b91",
    "f7edccd2-95d7-4fea-b511-ae5b3e309c7d",
    "ff0c34c3-bd33-4991-89f5-dfa96b69b26c",
    "461e2e52-50c9-4d77-a2f2-1cb77b601556"]>
to contain:
 <["foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91"]>
but could not find:
 <["foo", "bar"]>
 */
    }

    @Test
    public void name3() {
        assertThat(list).extracting("id").contains("foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91");
/*
 <["8665d91a-eab0-45a8-a378-e6603d66d084",
    "347caac1-daa2-4e31-bc95-e237ba1e2b91",
    "f7edccd2-95d7-4fea-b511-ae5b3e309c7d",
    "ff0c34c3-bd33-4991-89f5-dfa96b69b26c",
    "461e2e52-50c9-4d77-a2f2-1cb77b601556"]>
to contain:
 <["foo", "bar", "347caac1-daa2-4e31-bc95-e237ba1e2b91"]>
but could not find:
 <["foo", "bar"]>
 */
    }

    @Test
    public void categories1() {
        final Reference<Category> longSleevesCategory = Reference.of(Category.referenceTypeId(), "eab84062-f4d7-4dd6-a576-ce5c10923e68");
        assertThat(list)
                .hasSize(5)
                .contains(list.get(0))
                .filteredOn(productProjection -> productProjection.getCategories().contains(longSleevesCategory))
                .extracting("id")
                .contains("foo");
/*
java.lang.AssertionError:
Expecting:
  <["8665d91a-eab0-45a8-a378-e6603d66d084",
    "ff0c34c3-bd33-4991-89f5-dfa96b69b26c"]>
to contain only:
  <["foo"]>
elements not found:
  <["foo"]>
and elements not expected:
  <["8665d91a-eab0-45a8-a378-e6603d66d084",
    "ff0c34c3-bd33-4991-89f5-dfa96b69b26c"]>
 */
    }

    @Test
    public void categories2() {
        final Reference<Category> longSleevesCategory = Reference.of(Category.referenceTypeId(), "eab84062-f4d7-4dd6-a576-ce5c10923e68");
        assertThat(list)
                .hasSize(5)
                .contains(list.get(0))
                .filteredOn(productProjection -> productProjection.getCategories().contains(longSleevesCategory))
                .extracting("id")
                .contains("foo")
                //negated
                .doesNotContain("bar");
    }

    @Test
    public void categories3() {
        final Reference<Category> longSleevesCategory = Reference.of(Category.referenceTypeId(), "eab84062-f4d7-4dd6-a576-ce5c10923e68");
        assertThat(list)
                .hasSize(5)
                .contains(list.get(0))
                .filteredOn(productProjection -> productProjection.getCategories().contains(longSleevesCategory))
                .extracting("id", Integer.class)//typing possible
                .contains(1)//works only in compilation so here it is an integer, runtime type is ignored
                .doesNotContain(3);
/*
java.lang.AssertionError:
Expecting:
<["8665d91a-eab0-45a8-a378-e6603d66d084",
        "ff0c34c3-bd33-4991-89f5-dfa96b69b26c"]>
to contain:
<[1]>
but could not find:
<[1]>
*/
    }

    @Test
    public void properties() {
        assertThat(list)
                .extracting("id", "version")//multiple extracting parameters force the contains parameters to be of tuple
                .contains(
                        tuple("baz"),
                        tuple("foo", 3)
                );
/*
java.lang.AssertionError:
Expecting:
 <[("8665d91a-eab0-45a8-a378-e6603d66d084", 4L),
    ("347caac1-daa2-4e31-bc95-e237ba1e2b91", 4L),
    ("f7edccd2-95d7-4fea-b511-ae5b3e309c7d", 4L),
    ("ff0c34c3-bd33-4991-89f5-dfa96b69b26c", 4L),
    ("461e2e52-50c9-4d77-a2f2-1cb77b601556", 4L)]>
to contain:
 <[("foo"), ("foo", 3)]>
but could not find:
 <[("foo"), ("foo", 3)]>
 */
    }



    @Test
    public void m() {
        final ProductProjection productProjection = list.get(3);
        assertThat(productProjection.getId()).isEqualTo("461e2e52-50c9-4d77-a2f2-1cb77b601556");
        assertThat(productProjection.getProductType().getId()).isEqualTo("461e2e52-50c9-4d77-a2f2-1cb77b601556");
/*
org.junit.ComparisonFailure:
Expected :"461e2e52-50c9-4d77-a2f2-1cb77b601556"
Actual   :"ff0c34c3-bd33-4991-89f5-dfa96b69b26c"
 */

        //which one failed?
        //so you could split the method into two and use one assertion per test method
    }

    @Test
    public void m1() {
        final ProductProjection productProjection = list.get(3);
        assertThat(productProjection.getId()).as("id").isEqualTo("461e2e52-50c9-4d77-a2f2-1cb77b601556");
        assertThat(productProjection.getProductType().getId()).as("productType id").isEqualTo("461e2e52-50c9-4d77-a2f2-1cb77b601556");
/*
org.junit.ComparisonFailure: [id]
Expected :"461e2e52-50c9-4d77-a2f2-1cb77b601556"
Actual   :"ff0c34c3-bd33-4991-89f5-dfa96b69b26c"
 */

        //ah, it is the id
        //in fest assert overridingErrorMessage was used to achieve that, but the div was broken then
    }


    //use this so you don't forget to call assertAll()
    public static void softAssert(final Consumer<SoftAssertions> softlyConsumer) {
        final SoftAssertions softly = new SoftAssertions();
        softlyConsumer.accept(softly);
        softly.assertAll();
    }

    @Test
    public void softAssertions() {
        final Reference<Category> smartPhones = Reference.of(Category.referenceTypeId(), "xab84062-f4d7-4dd6-a576-ce5c10923e68");
        final ProductProjection productProjection = list.get(3);
        softAssert(softly -> {
            softly.assertThat(productProjection.getName().get(Locale.ENGLISH)).as("name").isEqualTo("Nexus 5");
            softly.assertThat(productProjection.getSlug().get(Locale.ENGLISH)).as("slug").isEqualTo("google-nexus-5");
            softly.assertThat(productProjection.getId()).as("id").isEqualTo("3b423f46-124e-4db5-b16a-1d68afabe640");
            softly.assertThat(productProjection.getCategories()).as("categories").contains(smartPhones);
        });
/*
org.assertj.core.api.SoftAssertionError:
The following 4 assertions failed:
1) [name] expected:<"[Nexus 5]"> but was:<"[MB PREMIUM TECH T]">
2) [slug] expected:<"[google-nexus-5]"> but was:<"[mb-premium-tech-t1417714797277]">
3) [id] expected:<"[3b423f46-124e-4db5-b16a-1d68afabe640]"> but was:<"[ff0c34c3-bd33-4991-89f5-dfa96b69b26c]">
4) [categories]
Expecting:
 <[Reference{typeId='category', id='eab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}]>
to contain:
 <[Reference{typeId='category', id='xab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}]>
but could not find:
 <[Reference{typeId='category', id='xab84062-f4d7-4dd6-a576-ce5c10923e68', obj=null}]>
 */
    }
}










