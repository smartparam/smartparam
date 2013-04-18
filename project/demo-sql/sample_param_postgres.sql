-- tested on POSTGRES

insert into smartparam_parameter
	(id, arrayseparator, cacheable, name, label, description, type, multivalue, nullable, archive, inputlevels, array_flag)
	values(nextval('seq_sp_parameter'), ',', true, 'sample', 'Sample param', 'Sample param description', 'string', false, false, false, 0, false);

insert into smartparam_level
	(id, label, labelkey, parameter_id, type, orderno, array_flag, levelcreator)
	values(nextval('seq_sp_level'), 'sample-level', 'smartparam.level.sample', 1, 'plugin', 0, false, 'level.sample');

insert into smartparam_parameter_entry
	(id, function, level1, level2, level3, level4, level5, level6, level7, level8, value, parameter_id )
	values(nextval('seq_sp_parameter_entry'), null, 'input1', null, null, null, null, null, null, null, 'output1', 1);
insert into smartparam_parameter_entry
	(id, function, level1, level2, level3, level4, level5, level6, level7, level8, value, parameter_id )
	values(nextval('seq_sp_parameter_entry'), null, 'input2', null, null, null, null, null, null, null, 'output2', 1);
	insert into smartparam_parameter_entry
	(id, function, level1, level2, level3, level4, level5, level6, level7, level8, value, parameter_id )
	values(nextval('seq_sp_parameter_entry'), null, 'input3', null, null, null, null, null, null, null, 'output3', 1);