package com.pinamar.api.repositorios;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.pinamar.api.negocio.Cliente;
import com.pinamar.api.negocio.Empleado;
import com.pinamar.api.negocio.EmpleadoFijo;
import com.pinamar.api.negocio.EmpleadoPorHora;
import com.pinamar.api.negocio.EmpleadoView;

@Repository
public class ClienteRepoImplementation implements ClienteRepositorio{

	private final MongoOperations mongoOp;
	
	@Autowired
	public ClienteRepoImplementation(MongoOperations mongoOperations) {
		this.mongoOp = mongoOperations;
	}
	
	public Optional<List<Cliente>> getAllClientes() {
		List<Cliente> clientes = this.mongoOp.find(new Query(), Cliente.class);
		return Optional.ofNullable(clientes);
	}
	public Optional<Cliente> findById(String id) {
		ObjectId _id = new ObjectId(id);
		Cliente c = this.mongoOp.findOne(new Query(Criteria.where("_id").is(_id)), Cliente.class);
		return Optional.ofNullable(c);
	}
	public Optional<Cliente> findByCuit(String cuit) {
		Cliente c = this.mongoOp.findOne(new Query(Criteria.where("cuit").is(cuit)), Cliente.class);
		return Optional.ofNullable(c);
	}
	public Cliente saveCliente(Cliente c) {
		this.mongoOp.save(c);
		return findById(c.getId()).get();
	}
	public void deleteCliente(String id) {
		ObjectId _id = new ObjectId(id);
		this.mongoOp.findAndRemove(new Query(Criteria.where("_id").is(_id)), Cliente.class);
	}
	public void updateCliente(Cliente c) {
		this.mongoOp.save(c);
	}
	
	public Optional<EmpleadoView> findEmpleadoById(String id) {
		ObjectId _id = new ObjectId(id);
		EmpleadoView e = this.mongoOp.findOne(new Query(Criteria.where("_id").is(_id)), EmpleadoView.class);
		return Optional.ofNullable(e);
	}
	public Empleado saveEmpleadoFijo(EmpleadoFijo e) {
		EmpleadoView aux = new EmpleadoView(new ObjectId(e.getId()), e.getDni(), e.getNombre(), e.getDireccion(), e.getPuesto(), e.getFechaIngreso(), "FIJO", e.getTipoLiquidacion(), 0, 0, e.getSueldoBase(), e.getHorasExtra(), e.getDiasAusentes(), e.getDiasEnfermedad(), e.getDiasVacaciones(), e.getFeriados(), e.getDiasTrabajados());
		this.mongoOp.save(aux);
		EmpleadoView emp = findEmpleadoById(aux.getId()).get();
		e.setId(new ObjectId(emp.getId()));
		return e;
	}

	public Empleado saveEmpleadoHora(EmpleadoPorHora e) {
		EmpleadoView aux = new EmpleadoView(new ObjectId(e.getId()), e.getDni(), e.getNombre(), e.getDireccion(), e.getPuesto(), e.getFechaIngreso(), "POR HORA", e.getTipoLiquidacion(), e.getValorHora(), e.getHorasTrabajadas(), 0, 0, 0, 0, 0, 0, 0);
		this.mongoOp.save(aux);
		EmpleadoView emp = findEmpleadoById(aux.getId()).get();
		e.setId(new ObjectId(emp.getId()));
		return e;
	}
	
}