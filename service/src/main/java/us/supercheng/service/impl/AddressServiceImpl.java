package us.supercheng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import us.supercheng.mapper.UserAddressMapper;
import us.supercheng.pojo.UserAddress;
import us.supercheng.service.AddressService;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private UserAddressMapper addressMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean setUserDefaultShippingAddr(String userId, String addressId) {
        try {
            UserAddress userAddress = new UserAddress();
            userAddress.setUserId(userId);
            userAddress.setIsDefault(1);

            for (UserAddress ua : this.addressMapper.select(userAddress)) {
                ua.setIsDefault(0);
                this.addressMapper.updateByPrimaryKeySelective(ua);
            }

            userAddress = new UserAddress();
            userAddress.setId(addressId);
            userAddress.setIsDefault(1);
            return this.addressMapper.updateByPrimaryKeySelective(userAddress) == 1;
        } catch (Exception ex) {
            return false;
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> getUserAddresses(String userId) {
        UserAddress addr = new UserAddress();
        addr.setUserId(userId);
        return this.addressMapper.select(addr);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean createAddr(UserAddress addr) {
        try {
            return this.addressMapper.insert(addr) == 1;
        } catch (Exception ex) {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean updateAddr(UserAddress addr) {
        try {
            return this.addressMapper.updateByPrimaryKeySelective(addr) == 1;
        } catch (Exception ex) {
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public boolean deleteUserAddr(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setId(addressId);

        try {
            return this.addressMapper.delete(userAddress) == 1;
        } catch (Exception ex) {
            return false;
        }
    }
}
