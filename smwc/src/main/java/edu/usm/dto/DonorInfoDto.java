package edu.usm.dto;

import edu.usm.domain.DonorInfo;

import java.io.Serializable;

/**
 * Created by scottkimball on 5/9/15.
 */
public class DonorInfoDto extends BasicEntityDto implements Serializable {

    public DonorInfo convertToDonorInfo () {
        return new DonorInfo();
    }
}
