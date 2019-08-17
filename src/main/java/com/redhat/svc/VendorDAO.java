package com.redhat.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.EntityNotFoundException;

@Component
public class VendorDAO {

	@Autowired
	private VendorRepository repository;

	public Vendor findVendor(Integer id) {
		Vendor vendor = repository.findOne(id);
		if (vendor == null) {
			throw new EntityNotFoundException("Cannot find vendor with id = " + id);
		}
		return vendor;
	}
}
