package edu.usm.query;

import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import edu.usm.domain.QContact;
import edu.usm.dto.PredicateDto;
import edu.usm.dto.QueryDto;

import java.util.List;

/**
 * Builds an {@link Predicate} from an {@link QueryDto}
 */
public class QueryBuilder {

    private static final String TRUE = "true";
    private static final String FALSE = "false";
    private static final char WILDCARD = '%';

    private Predicate predicate;

    public QueryBuilder() {
        this.predicate = QContact.contact.deleted.eq(false);
    }

    public Predicate buildQuery(QueryDto dto) throws QueryBuilderException {
        List<PredicateDto> predicateDtos = dto.getPredicateDtos();

        for (PredicateDto predicateDto : predicateDtos) {
            addPredicate(predicateDto);
        }

        return predicate;
    }

   private void addPredicate(PredicateDto predicateDto) throws QueryBuilderException {

       switch (predicateDto.getField()) {

           case Constants.FIRST_NAME:
               addStringPredicate(QContact.contact.firstName, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.MIDDLE_NAME:
               addStringPredicate(QContact.contact.middleName, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.LAST_NAME:
               addStringPredicate(QContact.contact.lastName, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.NICKNAME:
               addStringPredicate(QContact.contact.nickName, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.STREET_ADDRESS:
               addStringPredicate(QContact.contact.streetAddress, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.CITY:
               addStringPredicate(QContact.contact.city, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.STATE:
               addStringPredicate(QContact.contact.state, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.ZIP_CODE:
               addStringPredicate(QContact.contact.zipCode, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.MAILING_STREET_ADDRESS:
               addStringPredicate(QContact.contact.mailingStreetAddress, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.MAILING_CITY:
               addStringPredicate(QContact.contact.mailingCity, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.MAILING_STATE:
               addStringPredicate(QContact.contact.mailingState, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.MAILING_ZIP_CODE:
               addStringPredicate(QContact.contact.mailingZipCode, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.PHONE_NUMBER_1:
               addStringPredicate(QContact.contact.phoneNumber1, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.PHONE_NUMBER_2:
               addStringPredicate(QContact.contact.phoneNumber2, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.EMAIL:
               addStringPredicate(QContact.contact.email, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.LANGUAGE:
               addStringPredicate(QContact.contact.language, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.OCCUPATION:
               addStringPredicate(QContact.contact.occupation, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.DONOR:
               addBooleanPredicate(QContact.contact.donor, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.MEMBER:
               addBooleanPredicate(QContact.contact.member, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.ASSESSMENT:
               addIntegerPredicate(QContact.contact.assessment, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.INITIATOR:
               addBooleanPredicate(QContact.contact.initiator, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.RACE:
               addStringPredicate(QContact.contact.race, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.ETHNICITY:
               addStringPredicate(QContact.contact.ethnicity, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.GENDER:
               addStringPredicate(QContact.contact.gender, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.DISABLED:
               addBooleanPredicate(QContact.contact.disabled, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.SEXUAL_ORIENTATION:
               addStringPredicate(QContact.contact.sexualOrientation, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.INCOME_BRACKET:
               addStringPredicate(QContact.contact.incomeBracket, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.NEEDS_FOLLOW_UP:
               addBooleanPredicate(QContact.contact.needsFollowUp, predicateDto.getOperator(), predicateDto.getValue());
               break;

           case Constants.DATE_OF_BIRTH:
               //TODO should be a DateTime Field
               throw new QueryBuilderException("Unsupported Field");

           case Constants.CREATED:
               //TODO Should be a DateTime field
               throw new QueryBuilderException("Unsupported Field");

           case Constants.LAST_MODIFIED:
               //TODO Should be a DateTime field
               throw new QueryBuilderException("Unsupported Field");

           default:
               throw new QueryBuilderException("Unsupported Field");
       }
   }

    private void addStringPredicate(StringPath path, String operator, String value) throws QueryBuilderException {
        switch (operator) {

            case Constants.EQUALS:
                predicate = path.eq(value).and(predicate);
                break;

            case Constants.NOT_EQUALS:
                predicate = path.ne(value).and(predicate);
                break;

            case Constants.CONTAINS:
                predicate = path.like(WILDCARD + value + WILDCARD).and(predicate);
                break;

            default:
                throw new QueryBuilderException("Unsupported Operation");
        }

    }

    private void addIntegerPredicate (NumberPath<Integer> numberPath, String operator, String value) throws QueryBuilderException {
        try {
            int val = Integer.parseInt(value);

            switch (operator) {
                case Constants.LESS_THAN:
                    predicate = numberPath.lt(val).and(predicate);
                    break;

                case Constants.EQUALS:
                    predicate = numberPath.eq(val).and(predicate);
                    break;

                case Constants.GREATER_THAN:
                    predicate = numberPath.gt(val).and(predicate);
                    break;

                default:
                    throw new QueryBuilderException("Unsupported Operation");
            }

        } catch (NumberFormatException e) {
            throw new QueryBuilderException("Not an Integer Value: " + value);
        }
    }

    private void addBooleanPredicate (BooleanPath path, String operator, String value) throws QueryBuilderException {
        boolean val;
        if (value.equalsIgnoreCase(TRUE)) {
            val = true;
        } else if (value.equalsIgnoreCase(FALSE)) {
            val = false;
        } else {
            throw new QueryBuilderException("Not a boolean value: " + value);
        }

        switch (operator) {

            case Constants.EQUALS:
                predicate = path.eq(val).and(predicate);
                break;

            default:
                throw new QueryBuilderException("Unsupported Operation");
        }
    }
}
