package DAOs.APIDAOs;

import APIBeans.Taxonomy.TaxonomyAttractionsAPIJSON;
import APIBeans.Taxonomy.TaxonomyCategoriesAPIJSON;
import APIBeans.Taxonomy.TaxonomyDestinationsAPIJSON;
import APIBeans.Taxonomy.TaxonomyAttractionsPOST;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by George on 04/06/17.
 */
public class TaxonomyAPIDAO {

    private RestTemplate restTemplate;

    /**
     *
     * Retrieves Data from /service/taxonomy/destinations (Complete list of all available destinations)
     * API's web service and returns them within a JSON Object.If fails to
     * retrieve or something else went wrong it returns the object with property
     * success=false.
     */
    public TaxonomyDestinationsAPIJSON retrieveDestinations(){

        final String url =Helper.ProjectProperties.apiURL+ "/service/taxonomy/destinations?apiKey=" + Helper.ProjectProperties.apiKey;
        TaxonomyDestinationsAPIJSON taxonomyDestinationsAPIJSON =new TaxonomyDestinationsAPIJSON();
        taxonomyDestinationsAPIJSON.setSuccess(false);
        try {
            restTemplate = new RestTemplate();
            taxonomyDestinationsAPIJSON = restTemplate.getForObject(url,TaxonomyDestinationsAPIJSON.class);
        }
        catch ( HttpClientErrorException e) {
            System.out.println("*****************"+e.getStatusCode()+"*****************");
            System.out.println("*****************"+e.getResponseBodyAsString()+"*****************");
        }
        catch ( HttpServerErrorException e) {
            System.out.println("*****************"+e.getStatusCode()+"*****************");
            System.out.println("*****************"+e.getResponseBodyAsString()+"*****************");
        }
        catch( ResourceAccessException e2) {
            System.out.println("*****************"+e2.getMessage()+"*****************");
        }

        return taxonomyDestinationsAPIJSON;
    }

    /**
     *
     * Retrieves Data from /service/taxonomy/attractions (A list of available attractions
     * based on destinationId.Sortorder can be
     * SEO_PUBLISHED_DATE_D - Publish Date (Descending)
     * SEO_PUBLISHED_DATE_A - Publish Date (Ascending)
     * SEO_REVIEW_AVG_RATING_D - Traveler Rating (high→low)
     * SEO_REVIEW_AVG_RATING_A - Traveler Rating (low→high)
     * SEO_ALPHABETICAL - Alphabetical (A→Z).TopX is the size of the list)
     * API's web service and returns them within a JSON Object.If fails to
     * retrieve or something else went wrong it returns the object with property
     * success=false.
     */
    public TaxonomyAttractionsAPIJSON retrieveAttractions(int destinationId, String topX, String sortOrder){

        final String url =Helper.ProjectProperties.apiURL+ "/service/taxonomy/attractions?apiKey=" + Helper.ProjectProperties.apiKey;
        TaxonomyAttractionsPOST taxonomyAttractionsPOST =new TaxonomyAttractionsPOST();
        taxonomyAttractionsPOST.setDestId(destinationId);
        taxonomyAttractionsPOST.setSortOrder(sortOrder);
        taxonomyAttractionsPOST.setTopX(topX);
        TaxonomyAttractionsAPIJSON taxonomyAttractionsAPIJSON =new TaxonomyAttractionsAPIJSON();
        taxonomyAttractionsAPIJSON.setSuccess(false);
        try {
            restTemplate = new RestTemplate();
            taxonomyAttractionsAPIJSON = restTemplate.postForObject(url, taxonomyAttractionsPOST,TaxonomyAttractionsAPIJSON.class);
        }
        catch ( HttpClientErrorException e) {
            System.out.println("*****************"+e.getStatusCode()+"*****************");
            System.out.println("*****************"+e.getResponseBodyAsString()+"*****************");
        }
        catch ( HttpServerErrorException e) {
            System.out.println("*****************"+e.getStatusCode()+"*****************");
            System.out.println("*****************"+e.getResponseBodyAsString()+"*****************");
        }
        catch( ResourceAccessException e2) {
            System.out.println("*****************"+e2.getMessage()+"*****************");
        }

        return taxonomyAttractionsAPIJSON;
    }

    /**
     *
     * Retrieves Data from /service/taxonomy/categories (Complete list of all available categories)
     * API's web service and returns them within a JSON Object.If fails to
     * retrieve or something else went wrong it returns the object with property
     * success=false.
     */
    public TaxonomyCategoriesAPIJSON retrieve_categories(){

        final String url =Helper.ProjectProperties.apiURL+ "/service/taxonomy/categories?apiKey=" + Helper.ProjectProperties.apiKey;
        TaxonomyCategoriesAPIJSON taxonomyCategoriesAPIJSON =new TaxonomyCategoriesAPIJSON();
        taxonomyCategoriesAPIJSON.setSuccess(false);
        try {
            restTemplate = new RestTemplate();
            taxonomyCategoriesAPIJSON = restTemplate.getForObject(url,TaxonomyCategoriesAPIJSON.class);
        }
        catch ( HttpClientErrorException e) {
            System.out.println("*****************"+e.getStatusCode()+"*****************");
            System.out.println("*****************"+e.getResponseBodyAsString()+"*****************");
        }
        catch ( HttpServerErrorException e) {
            System.out.println("*****************"+e.getStatusCode()+"*****************");
            System.out.println("*****************"+e.getResponseBodyAsString()+"*****************");
        }
        catch( ResourceAccessException e2) {
            System.out.println("*****************"+e2.getMessage()+"*****************");
        }

        return taxonomyCategoriesAPIJSON;
    }
}
