pragma Warnings (Off);
pragma Ada_95;
pragma Source_File_Name (ada_main, Spec_File_Name => "b__test_module_abr.ads");
pragma Source_File_Name (ada_main, Body_File_Name => "b__test_module_abr.adb");
pragma Suppress (Overflow_Check);
with Ada.Exceptions;

package body ada_main is

   E070 : Short_Integer; pragma Import (Ada, E070, "system__os_lib_E");
   E018 : Short_Integer; pragma Import (Ada, E018, "ada__exceptions_E");
   E014 : Short_Integer; pragma Import (Ada, E014, "system__soft_links_E");
   E012 : Short_Integer; pragma Import (Ada, E012, "system__exception_table_E");
   E035 : Short_Integer; pragma Import (Ada, E035, "ada__containers_E");
   E065 : Short_Integer; pragma Import (Ada, E065, "ada__io_exceptions_E");
   E009 : Short_Integer; pragma Import (Ada, E009, "ada__strings_E");
   E053 : Short_Integer; pragma Import (Ada, E053, "ada__strings__maps_E");
   E057 : Short_Integer; pragma Import (Ada, E057, "ada__strings__maps__constants_E");
   E040 : Short_Integer; pragma Import (Ada, E040, "interfaces__c_E");
   E021 : Short_Integer; pragma Import (Ada, E021, "system__exceptions_E");
   E076 : Short_Integer; pragma Import (Ada, E076, "system__object_reader_E");
   E047 : Short_Integer; pragma Import (Ada, E047, "system__dwarf_lines_E");
   E092 : Short_Integer; pragma Import (Ada, E092, "system__soft_links__initialize_E");
   E034 : Short_Integer; pragma Import (Ada, E034, "system__traceback__symbolic_E");
   E146 : Short_Integer; pragma Import (Ada, E146, "ada__assertions_E");
   E096 : Short_Integer; pragma Import (Ada, E096, "ada__strings__utf_encoding_E");
   E102 : Short_Integer; pragma Import (Ada, E102, "ada__tags_E");
   E008 : Short_Integer; pragma Import (Ada, E008, "ada__strings__text_buffers_E");
   E110 : Short_Integer; pragma Import (Ada, E110, "ada__streams_E");
   E122 : Short_Integer; pragma Import (Ada, E122, "system__file_control_block_E");
   E121 : Short_Integer; pragma Import (Ada, E121, "system__finalization_root_E");
   E119 : Short_Integer; pragma Import (Ada, E119, "ada__finalization_E");
   E118 : Short_Integer; pragma Import (Ada, E118, "system__file_io_E");
   E150 : Short_Integer; pragma Import (Ada, E150, "system__storage_pools_E");
   E148 : Short_Integer; pragma Import (Ada, E148, "system__finalization_masters_E");
   E156 : Short_Integer; pragma Import (Ada, E156, "system__storage_pools__subpools_E");
   E125 : Short_Integer; pragma Import (Ada, E125, "ada__strings__unbounded_E");
   E108 : Short_Integer; pragma Import (Ada, E108, "ada__text_io_E");
   E152 : Short_Integer; pragma Import (Ada, E152, "system__pool_global_E");
   E123 : Short_Integer; pragma Import (Ada, E123, "sda_exceptions_E");
   E142 : Short_Integer; pragma Import (Ada, E142, "lca_E");
   E005 : Short_Integer; pragma Import (Ada, E005, "abr_E");

   Sec_Default_Sized_Stacks : array (1 .. 1) of aliased System.Secondary_Stack.SS_Stack (System.Parameters.Runtime_Default_Sec_Stack_Size);

   Local_Priority_Specific_Dispatching : constant String := "";
   Local_Interrupt_States : constant String := "";

   Is_Elaborated : Boolean := False;

   procedure finalize_library is
   begin
      E152 := E152 - 1;
      declare
         procedure F1;
         pragma Import (Ada, F1, "system__pool_global__finalize_spec");
      begin
         F1;
      end;
      E108 := E108 - 1;
      declare
         procedure F2;
         pragma Import (Ada, F2, "ada__text_io__finalize_spec");
      begin
         F2;
      end;
      E125 := E125 - 1;
      declare
         procedure F3;
         pragma Import (Ada, F3, "ada__strings__unbounded__finalize_spec");
      begin
         F3;
      end;
      E156 := E156 - 1;
      declare
         procedure F4;
         pragma Import (Ada, F4, "system__storage_pools__subpools__finalize_spec");
      begin
         F4;
      end;
      E148 := E148 - 1;
      declare
         procedure F5;
         pragma Import (Ada, F5, "system__finalization_masters__finalize_spec");
      begin
         F5;
      end;
      declare
         procedure F6;
         pragma Import (Ada, F6, "system__file_io__finalize_body");
      begin
         E118 := E118 - 1;
         F6;
      end;
      declare
         procedure Reraise_Library_Exception_If_Any;
            pragma Import (Ada, Reraise_Library_Exception_If_Any, "__gnat_reraise_library_exception_if_any");
      begin
         Reraise_Library_Exception_If_Any;
      end;
   end finalize_library;

   procedure adafinal is
      procedure s_stalib_adafinal;
      pragma Import (Ada, s_stalib_adafinal, "system__standard_library__adafinal");

      procedure Runtime_Finalize;
      pragma Import (C, Runtime_Finalize, "__gnat_runtime_finalize");

   begin
      if not Is_Elaborated then
         return;
      end if;
      Is_Elaborated := False;
      Runtime_Finalize;
      s_stalib_adafinal;
   end adafinal;

   type No_Param_Proc is access procedure;
   pragma Favor_Top_Level (No_Param_Proc);

   procedure adainit is
      Main_Priority : Integer;
      pragma Import (C, Main_Priority, "__gl_main_priority");
      Time_Slice_Value : Integer;
      pragma Import (C, Time_Slice_Value, "__gl_time_slice_val");
      WC_Encoding : Character;
      pragma Import (C, WC_Encoding, "__gl_wc_encoding");
      Locking_Policy : Character;
      pragma Import (C, Locking_Policy, "__gl_locking_policy");
      Queuing_Policy : Character;
      pragma Import (C, Queuing_Policy, "__gl_queuing_policy");
      Task_Dispatching_Policy : Character;
      pragma Import (C, Task_Dispatching_Policy, "__gl_task_dispatching_policy");
      Priority_Specific_Dispatching : System.Address;
      pragma Import (C, Priority_Specific_Dispatching, "__gl_priority_specific_dispatching");
      Num_Specific_Dispatching : Integer;
      pragma Import (C, Num_Specific_Dispatching, "__gl_num_specific_dispatching");
      Main_CPU : Integer;
      pragma Import (C, Main_CPU, "__gl_main_cpu");
      Interrupt_States : System.Address;
      pragma Import (C, Interrupt_States, "__gl_interrupt_states");
      Num_Interrupt_States : Integer;
      pragma Import (C, Num_Interrupt_States, "__gl_num_interrupt_states");
      Unreserve_All_Interrupts : Integer;
      pragma Import (C, Unreserve_All_Interrupts, "__gl_unreserve_all_interrupts");
      Exception_Tracebacks : Integer;
      pragma Import (C, Exception_Tracebacks, "__gl_exception_tracebacks");
      Detect_Blocking : Integer;
      pragma Import (C, Detect_Blocking, "__gl_detect_blocking");
      Default_Stack_Size : Integer;
      pragma Import (C, Default_Stack_Size, "__gl_default_stack_size");
      Default_Secondary_Stack_Size : System.Parameters.Size_Type;
      pragma Import (C, Default_Secondary_Stack_Size, "__gnat_default_ss_size");
      Bind_Env_Addr : System.Address;
      pragma Import (C, Bind_Env_Addr, "__gl_bind_env_addr");

      procedure Runtime_Initialize (Install_Handler : Integer);
      pragma Import (C, Runtime_Initialize, "__gnat_runtime_initialize");

      Finalize_Library_Objects : No_Param_Proc;
      pragma Import (C, Finalize_Library_Objects, "__gnat_finalize_library_objects");
      Binder_Sec_Stacks_Count : Natural;
      pragma Import (Ada, Binder_Sec_Stacks_Count, "__gnat_binder_ss_count");
      Default_Sized_SS_Pool : System.Address;
      pragma Import (Ada, Default_Sized_SS_Pool, "__gnat_default_ss_pool");

   begin
      if Is_Elaborated then
         return;
      end if;
      Is_Elaborated := True;
      Main_Priority := -1;
      Time_Slice_Value := -1;
      WC_Encoding := 'b';
      Locking_Policy := ' ';
      Queuing_Policy := ' ';
      Task_Dispatching_Policy := ' ';
      Priority_Specific_Dispatching :=
        Local_Priority_Specific_Dispatching'Address;
      Num_Specific_Dispatching := 0;
      Main_CPU := -1;
      Interrupt_States := Local_Interrupt_States'Address;
      Num_Interrupt_States := 0;
      Unreserve_All_Interrupts := 0;
      Exception_Tracebacks := 1;
      Detect_Blocking := 0;
      Default_Stack_Size := -1;

      ada_main'Elab_Body;
      Default_Secondary_Stack_Size := System.Parameters.Runtime_Default_Sec_Stack_Size;
      Binder_Sec_Stacks_Count := 1;
      Default_Sized_SS_Pool := Sec_Default_Sized_Stacks'Address;

      Runtime_Initialize (1);

      Finalize_Library_Objects := finalize_library'access;

      Ada.Exceptions'Elab_Spec;
      System.Soft_Links'Elab_Spec;
      System.Exception_Table'Elab_Body;
      E012 := E012 + 1;
      Ada.Containers'Elab_Spec;
      E035 := E035 + 1;
      Ada.Io_Exceptions'Elab_Spec;
      E065 := E065 + 1;
      Ada.Strings'Elab_Spec;
      E009 := E009 + 1;
      Ada.Strings.Maps'Elab_Spec;
      E053 := E053 + 1;
      Ada.Strings.Maps.Constants'Elab_Spec;
      E057 := E057 + 1;
      Interfaces.C'Elab_Spec;
      E040 := E040 + 1;
      System.Exceptions'Elab_Spec;
      E021 := E021 + 1;
      System.Object_Reader'Elab_Spec;
      E076 := E076 + 1;
      System.Dwarf_Lines'Elab_Spec;
      E047 := E047 + 1;
      System.Os_Lib'Elab_Body;
      E070 := E070 + 1;
      System.Soft_Links.Initialize'Elab_Body;
      E092 := E092 + 1;
      E014 := E014 + 1;
      System.Traceback.Symbolic'Elab_Body;
      E034 := E034 + 1;
      E018 := E018 + 1;
      Ada.Assertions'Elab_Spec;
      E146 := E146 + 1;
      Ada.Strings.Utf_Encoding'Elab_Spec;
      E096 := E096 + 1;
      Ada.Tags'Elab_Spec;
      Ada.Tags'Elab_Body;
      E102 := E102 + 1;
      Ada.Strings.Text_Buffers'Elab_Spec;
      Ada.Strings.Text_Buffers'Elab_Body;
      E008 := E008 + 1;
      Ada.Streams'Elab_Spec;
      E110 := E110 + 1;
      System.File_Control_Block'Elab_Spec;
      E122 := E122 + 1;
      System.Finalization_Root'Elab_Spec;
      System.Finalization_Root'Elab_Body;
      E121 := E121 + 1;
      Ada.Finalization'Elab_Spec;
      E119 := E119 + 1;
      System.File_Io'Elab_Body;
      E118 := E118 + 1;
      System.Storage_Pools'Elab_Spec;
      E150 := E150 + 1;
      System.Finalization_Masters'Elab_Spec;
      System.Finalization_Masters'Elab_Body;
      E148 := E148 + 1;
      System.Storage_Pools.Subpools'Elab_Spec;
      System.Storage_Pools.Subpools'Elab_Body;
      E156 := E156 + 1;
      Ada.Strings.Unbounded'Elab_Spec;
      Ada.Strings.Unbounded'Elab_Body;
      E125 := E125 + 1;
      Ada.Text_Io'Elab_Spec;
      Ada.Text_Io'Elab_Body;
      E108 := E108 + 1;
      System.Pool_Global'Elab_Spec;
      System.Pool_Global'Elab_Body;
      E152 := E152 + 1;
      Sda_Exceptions'Elab_Spec;
      E123 := E123 + 1;
      E142 := E142 + 1;
      E005 := E005 + 1;
   end adainit;

   procedure Ada_Main_Program;
   pragma Import (Ada, Ada_Main_Program, "_ada_test_module_abr");

   function main
     (argc : Integer;
      argv : System.Address;
      envp : System.Address)
      return Integer
   is
      procedure Initialize (Addr : System.Address);
      pragma Import (C, Initialize, "__gnat_initialize");

      procedure Finalize;
      pragma Import (C, Finalize, "__gnat_finalize");
      SEH : aliased array (1 .. 2) of Integer;

      Ensure_Reference : aliased System.Address := Ada_Main_Program_Name'Address;
      pragma Volatile (Ensure_Reference);

   begin
      if gnat_argc = 0 then
         gnat_argc := argc;
         gnat_argv := argv;
      end if;
      gnat_envp := envp;

      Initialize (SEH'Address);
      adainit;
      Ada_Main_Program;
      adafinal;
      Finalize;
      return (gnat_exit_status);
   end;

--  BEGIN Object file/option list
   --   /home/nbelmakh/1A/pim/CD09/src/sda_exceptions.o
   --   /home/nbelmakh/1A/pim/CD09/src/lca.o
   --   /home/nbelmakh/1A/pim/CD09/src/abr.o
   --   /home/nbelmakh/1A/pim/CD09/src/test_module_abr.o
   --   -L/home/nbelmakh/1A/pim/CD09/src/
   --   -L/home/nbelmakh/1A/pim/CD09/src/
   --   -L/mnt/n7fs/ens/tp_cregut/GNAT/2021/lib/gcc/x86_64-pc-linux-gnu/10.3.1/adalib/
   --   -static
   --   -lgnat
   --   -ldl
--  END Object file/option list   

end ada_main;