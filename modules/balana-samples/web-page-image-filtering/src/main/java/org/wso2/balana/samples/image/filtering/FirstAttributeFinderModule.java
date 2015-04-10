package org.wso2.balana.samples.image.filtering;

import org.wso2.balana.attr.AttributeValue;
import org.wso2.balana.attr.BagAttribute;
import org.wso2.balana.attr.StringAttribute;
import org.wso2.balana.cond.EvaluationResult;
import org.wso2.balana.ctx.EvaluationCtx;
import org.wso2.balana.finder.AttributeFinderModule;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by farazath on 4/10/15.
 */
public class FirstAttributeFinderModule extends AttributeFinderModule{

    private URI defaultSubjectId;

    public FirstAttributeFinderModule(){
        try {
            defaultSubjectId = new URI("urn:oasis:names:tc:xacml:1.0:subject:subject-id");
        } catch (URISyntaxException e) {
            //ignore
        }
    }

    @Override
    public boolean isDesignatorSupported() {
        return true;
    }

    @Override
    public EvaluationResult findAttribute(URI attributeType, URI attributeId, String issuer, URI category, EvaluationCtx context) {
        String roleName = null;
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();

        EvaluationResult result = context.getAttribute(attributeType, defaultSubjectId, issuer, category);
        if(result != null && result.getAttributeValue() != null && result.getAttributeValue().isBag()){
            BagAttribute bagAttribute = (BagAttribute) result.getAttributeValue();
            if(bagAttribute.size() > 0){
                String userName = ((AttributeValue) bagAttribute.iterator().next()).encode();
                roleName = findRole(userName);
            }
        }

        if (roleName != null) {
            attributeValues.add(new StringAttribute(roleName));
        }

        System.out.println("First Attribute Finder Module found the attributes !!!");
        return new EvaluationResult(new BagAttribute(attributeType, attributeValues));
    }

    @Override
    public Set<String> getSupportedCategories() {
        Set<String> categories = new HashSet<String>();
        categories.add("urn:oasis:names:tc:xacml:1.0:subject-category:access-subject");
        return categories;
    }

    @Override
    public Set getSupportedIds() {
        Set<String> ids = new HashSet<String>();
        ids.add("http://wso2.org/attribute/roleNames");
        return ids;
    }

    private String findRole(String userName){

        System.out.println("First Attribute Module was hit !!!");
        if(userName.equals("bob")){
            return "InvalidA1";
        } else if(userName.equals("alice")){
            return "InvalidA2";
        } else if(userName.equals("peter")){
            return "InvalidA3";
        }

        return null;
    }

}
