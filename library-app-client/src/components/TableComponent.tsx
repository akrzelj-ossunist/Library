import {
  flexRender,
  getCoreRowModel,
  useReactTable,
} from "@tanstack/react-table";

const TableComponent: React.FC<{
  data: any;
  columns: any;
}> = ({ data, columns }) => {
  const { getHeaderGroups, getRowModel } = useReactTable({
    data: data,
    columns,
    debugTable: true,
    getCoreRowModel: getCoreRowModel(),
  });

  return (
    <table className="w-[80%] shadow-lg mt-10 tablet:w-[300px] table-auto">
      <thead className="bg-gray-200 font-bold text-gray-500 text-md tablet:hidden">
        {getHeaderGroups().map((headerGroup) => (
          <tr key={headerGroup.id} className="border-[1px] border-gray-300">
            {headerGroup.headers.map((header) => (
              <th key={header.id} className="p-5 w-[20%] text-center">
                <div>
                  {flexRender(
                    header.column.columnDef.header,
                    header.getContext()
                  )}
                </div>
              </th>
            ))}
          </tr>
        ))}
      </thead>
      <tbody>
        {getRowModel().rows.map((row) => (
          <tr
            key={row.id}
            className="border-[1px] border-gray-300 tablet:flex tablet:flex-col tablet:items-center  tablet:pl-2 tablet:pr-6 tablet:pb-6 tablet:pt-2 tablet:mb-4">
            {row.getVisibleCells().map((cell) => {
              return cell.getContext().column.id === "isAvailable" ? (
                <>
                  <label className="desktop:hidden text-xs text-blue-500 font-semibold mt-2">
                    {cell.id.substring(2).toUpperCase()}
                  </label>
                  <td
                    key={cell.id}
                    className="w-[20%] p-5 font-medium text-md tablet:w-full text-center tablet:pt-0">
                    {cell.getValue() === false ? "Unavailable" : "Available"}
                  </td>
                </>
              ) : (
                <>
                  <label className="desktop:hidden text-xs text-blue-500 font-semibold mt-2">
                    {cell.id.substring(2).toUpperCase()}
                  </label>
                  <td
                    key={cell.id}
                    className={`w-[20%] p-5 font-medium text-md tablet:w-full text-center tablet:pt-0 ${
                      cell.getContext().column.id === "title" && "w-[30%]"
                    }`}>
                    {flexRender(cell.column.columnDef.cell, cell.getContext())}
                  </td>
                </>
              );
            })}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default TableComponent;
