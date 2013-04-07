-- tested on POSTGRES

insert into smartparam_function_impl values('java', nextval('seq_sp_function_impl'), 'org.smartparam.demo.param.LevelFunction', 'demoModelObject');
insert into smartparam_function values(nextval('seq_sp_function'), true, 'level.sample', false, 'demoType', false, 1);

insert into smartparam_parameter values(nextval('seq_sp_parameter'), false, false, ',', true, 'Sample param description', 0, 'Sample param', false, 'sample', false, 'string');

insert into smartparam_level values(nextval('seq_sp_level'), false, 'sample-level', 'smartparam.level.sample', null, 0, 'plugin', 1, 1, null);

insert into smartparam_parameter_entry values(nextval('seq_sp_parameter_entry'), 'input1', null, null, null, null, null, null, null, 'output1', null, 1);
insert into smartparam_parameter_entry values(nextval('seq_sp_parameter_entry'), 'input2', null, null, null, null, null, null, null, 'output2', null, 1);
insert into smartparam_parameter_entry values(nextval('seq_sp_parameter_entry'), 'input3', null, null, null, null, null, null, null, 'output3', null, 1);